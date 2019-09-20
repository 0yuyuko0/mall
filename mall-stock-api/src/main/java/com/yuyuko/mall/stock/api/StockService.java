package com.yuyuko.mall.stock.api;

import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.stock.param.StockDeductParam;

import java.util.List;

public interface StockService {
    void deductStock(List<StockDeductParam> stockDeductParams) throws StockNotEnoughException;

    List<Integer> listStocks(List<Long> productIds);

    Integer getStock(Long productId);

    void createProductStock(StockCreateParam createParam);
}