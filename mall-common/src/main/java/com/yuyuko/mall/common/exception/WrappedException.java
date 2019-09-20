package com.yuyuko.mall.common.exception;

public class WrappedException extends RuntimeException {
    public WrappedException(RuntimeException e) {
        super(e.getMessage(), null, true, false);
    }
}
