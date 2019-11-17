package com.yuyuko.mall.stock.service.tcc;

import com.alibaba.fastjson.JSONArray;
import com.yuyuko.idempotent.api.IdempotentApi;
import com.yuyuko.idempotent.api.IdempotentInfo;
import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.manager.StockManager;
import com.yuyuko.mall.stock.param.StockDeductParam;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockDeductTccActionImpl implements StockDeductTccAction {
    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockManager stockManager;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private void batchDeductStock(List<StockDeductParam> stockDeductParams) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            for (StockDeductParam stockDeductParam : stockDeductParams)
                sqlSession.update("com.yuyuko.mall.stock.dao.StockDao.deductStock",
                        stockDeductParam);
            sqlSession.flushStatements();
        } catch (Throwable ex) {
            sqlSession.rollback();
            throw ex;
        }
    }

    private boolean checkStock(List<StockDeductParam> stockDeductParams) {
        List<Integer> stocks =
                stockManager.listStocks(stockDeductParams.stream().map(StockDeductParam::getProductId).collect(Collectors.toList()));
        return CollectionUtils.test(stockDeductParams, stocks,
                (stockDeductParam, stock) -> stockDeductParam.getCount() <= stock
        );
    }

    private boolean checkStockInDB(List<StockDeductParam> stockDeductParams) {
        List<Long> productIds =
                stockDeductParams.stream().map(StockDeductParam::getProductId).collect(Collectors.toList());
        return stockDao.listStocks(productIds).stream()
                .noneMatch(stockDTO -> stockDTO.getStock() < 0);
    }

    @Override
    public boolean prepare(BusinessActionContext actionContext,
                           List<StockDeductParam> stockDeductParams,
                           long uniqueId) throws StockNotEnoughException,
            StockDeductRollbackException {
        //先从缓存查库存，可以过滤掉大量无效请求
        if (!checkStock(stockDeductParams))
            throw new StockNotEnoughException();
        batchDeductStock(stockDeductParams);
        //扣完再查，超额则触发回滚
        if (!checkStockInDB(stockDeductParams))
            throw new StockDeductRollbackException();
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        return true;
    }

    @Autowired
    private IdempotentApi idempotentApi;

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        JSONArray stockDeductParamsJsonArray = ((JSONArray) actionContext.getActionContext().get(
                "stockDeductParams"));
        //空回滚
        if (stockDeductParamsJsonArray == null)
            return true;
        List<StockDeductParam> stockDeductParams =
                stockDeductParamsJsonArray.toJavaList(StockDeductParam.class);
        //加回去
        stockDeductParams.forEach(stockDeductParam -> stockDeductParam.setCount(-1 * stockDeductParam.getCount()));

        //幂等准备
        String uniqueId = ((Long) actionContext.getActionContext().get("id")).toString();
        IdempotentInfo idempotentInfo =
                IdempotentInfo.IdempotentInfoBuilder.builder()
                        .id(uniqueId)
                        .build();
        idempotentApi.prepare(idempotentInfo);
        try {
            batchDeductStock(stockDeductParams);
        } catch (Throwable throwable) {
            idempotentApi.afterThrowing(uniqueId);
        }
        idempotentApi.after(idempotentInfo);
        return true;
    }
}
