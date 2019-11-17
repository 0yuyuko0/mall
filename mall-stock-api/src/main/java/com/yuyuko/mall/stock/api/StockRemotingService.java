package com.yuyuko.mall.stock.api;

import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.stock.param.StockDeductParam;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface StockRemotingService {
    void deductStock(@NotEmpty List<StockDeductParam> stockDeductParams) throws StockNotEnoughException;

    List<Integer> listStocks(@NotEmpty List<Long> productIds);

    Integer getStock(@NotNull Long productId);

    void createProductStock(@NotNull StockCreateParam createParam);
}