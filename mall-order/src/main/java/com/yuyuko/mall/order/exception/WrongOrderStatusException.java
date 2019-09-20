package com.yuyuko.mall.order.exception;

public class WrongOrderStatusException extends Exception {
    public WrongOrderStatusException() {
        super(null, null, true, false);
    }
}
