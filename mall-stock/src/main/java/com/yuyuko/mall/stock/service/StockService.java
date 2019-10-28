package com.yuyuko.mall.stock.service;

import com.yuyuko.idempotent.annotation.Idempotent;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.product.message.ProductCreateMessage;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.entity.StockDO;
import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.manager.StockManager;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.stock.param.StockDeductParam;
import com.yuyuko.mall.stock.tcc.StockDeductTccAction;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    @Autowired
    StockManager stockManager;

    @Autowired
    StockDao stockDao;

    @Autowired
    IdGenerator idGenerator;

    @Autowired
    private MessageCodec messageCodec;

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
        //tcc扣库存
        stockDeductTccAction.prepare(null, stockDeductParams, idGenerator.nextId());
    }

    @RocketMQMessageListener(
            consumerGroup = "stock-product",
            topic = "product",
            selectorExpression = "create"
    )
    @Service
    public class ProductCreateMessageListener implements RocketMQListener<MessageExt> {
        @Override
        @Idempotent(id = "#message.getKeys()")
        public void onMessage(MessageExt message) {
            ProductCreateMessage createMessage = messageCodec.decode(message.getBody(),
                    ProductCreateMessage.class);
            stockDao.insert(buildStockDO(createMessage));
        }

        private StockDO buildStockDO(ProductCreateMessage createMessage) {
            StockDO stockDO = new StockDO();
            stockDO.setId(idGenerator.nextId());
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
        stockDO.setId(idGenerator.nextId());
        return stockDO;
    }
}