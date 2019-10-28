package com.yuyuko.mall.stock.api.impl;

import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.stock.param.StockDeductParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Slf4j
public class StockRemotingServiceImpl implements StockRemotingService {
    @Autowired
    com.yuyuko.mall.stock.service.StockService stockService;

    @Override
    public void deductStock(List<StockDeductParam> stockDeductParams) throws StockNotEnoughException {
        try {
            stockService.deductStock(stockDeductParams);
        } catch (StockDeductRollbackException ignore) {
        }
    }

    @Override
    public List<Integer> listStocks(List<Long> productIds) {
        return stockService.listStocks(productIds);
    }

    @Override
    public Integer getStock(Long productId) {
        return stockService.getStock(productId);
    }

    @Override
    public void createProductStock(StockCreateParam createParam) {
        stockService.createProductStock(createParam);
    }
}