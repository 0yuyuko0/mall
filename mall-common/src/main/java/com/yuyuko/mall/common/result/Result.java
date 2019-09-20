package com.yuyuko.mall.common.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    protected int code;
    protected String message;
    protected T data;

    public Result() {
        this.code = 0;
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> code(int code) {
        this.code = code;
        return this;
    }
}
