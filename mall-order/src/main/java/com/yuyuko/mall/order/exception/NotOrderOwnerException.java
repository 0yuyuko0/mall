package com.yuyuko.mall.order.exception;

public class NotOrderOwnerException extends Exception {
    public NotOrderOwnerException() {
        super(null, null, true, false);
    }
}
