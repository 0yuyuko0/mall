package com.yuyuko.mall.stock.api.impl;

import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.entity.StockDO;
import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.manager.StockManager;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.stock.param.StockDeductParam;
import com.yuyuko.mall.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Slf4j
public class StockRemotingServiceImpl implements StockRemotingService {
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockManager stockManager;

    @Override
    public void deductStock(List<StockDeductParam> stockDeductParams) throws StockNotEnoughException {
        try {
            stockService.deductStock(stockDeductParams);
        } catch (StockDeductRollbackException ignore) {
            throw new StockNotEnoughException();
        }
    }

    @Override
    public List<Integer> listStocks(List<Long> productIds) {
        return stockManager.listStocks(productIds);
    }

    @Override
    public Integer getStock(Long productId) {
        return stockManager.getStock(productId);
    }

    @Override
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