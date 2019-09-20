package com.yuyuko.mall.stock.exception;

//使用seata时标识回滚的类
public class StockDeductRollbackException extends Exception {
    public StockDeductRollbackException() {
        super(null, null, true, false);
    }
}
