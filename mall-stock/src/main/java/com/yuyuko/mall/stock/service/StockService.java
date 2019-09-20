package com.yuyuko.mall.stock.service;

import com.yuyuko.mall.common.utils.ProtoStuffUtils;
import com.yuyuko.mall.common.utils.SnowflakeIdGenerator;
import com.yuyuko.mall.product.message.ProductCreateMessage;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.entity.StockDO;
import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.manager.StockManager;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.stock.param.StockDeductParam;
import com.yuyuko.mall.stock.tcc.StockDeductTccAction;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StockService {
    @Autowired
    StockManager stockManager;

    @Autowired
    StockDao stockDao;

    @Autowired
    SnowflakeIdGenerator snowflakeIdGenerator;

    public Integer getStock(Long productId) {
        return stockManager.getStock(productId);
    }

    public List<Integer> listStocks(List<Long> productIds) {
        return stockManager.listStocks(productIds);
    }

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    StockDeductTccAction stockDeductTccAction;

    @GlobalTransactional(rollbackFor = StockDeductRollbackException.class, noRollbackFor =
            StockNotEnoughException.class)
    public void deductStock(List<StockDeductParam> stockDeductParams) throws StockNotEnoughException, StockDeductRollbackException {
        stockDeductTccAction.prepare(null, stockDeductParams, snowflakeIdGenerator.nextId());
    }

    @RocketMQMessageListener(
            consumerGroup = "stock-product",
            topic = "product",
            selectorExpression = "create"
    )
    @Service
    public class ProductCreateMessageListener implements RocketMQListener<MessageExt> {
        @Override
        public void onMessage(MessageExt message) {
            ProductCreateMessage createMessage = ProtoStuffUtils.deserialize(message.getBody(),
                    ProductCreateMessage.class);
            stockDao.insert(buildStockDO(createMessage));
        }

        private StockDO buildStockDO(ProductCreateMessage createMessage) {
            StockDO stockDO = new StockDO();
            stockDO.setId(snowflakeIdGenerator.nextId());
            stockDO.setProductId(createMessage.getId());
            stockDO.setStock(createMessage.getStock());
            return stockDO;
        }
    }

    public void createProductStock(StockCreateParam createParam) {
        StockDO stockDO = buildStockDO(createParam);
        stockDao.insert(stockDO);
    }

    private StockDO buildStockDO(StockCreateParam createParam) {
        StockDO stockDO = new StockDO();
        BeanUtils.copyProperties(createParam, stockDO);
        stockDO.setId(snowflakeIdGenerator.nextId());
        return stockDO;
    }
}