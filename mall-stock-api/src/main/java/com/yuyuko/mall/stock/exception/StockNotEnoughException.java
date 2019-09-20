package com.yuyuko.mall.stock.exception;

public class StockNotEnoughException extends Exception {
    public StockNotEnoughException() {
        super(null, null, true, false);
    }
}
