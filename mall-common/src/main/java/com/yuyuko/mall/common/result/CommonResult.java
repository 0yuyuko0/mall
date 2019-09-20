package com.yuyuko.mall.common.result;

public class CommonResult<T>{
    public static final int SUCCESS = 200;

    public static final int FAILED = 500;

    public static final int DATA_BINDING_FAILED = 600;

    public static final int UNAUTHORIZED = 401;

    public static final int ACCESS_DENIED = 403;

    public static <T> Result<T> success() {
        return new Result<>(SUCCESS,"成功");
    }

    public static <T> Result<T> failed() {
        return new Result<>(FAILED,"失败");
    }

    public static <T> Result<T> dataBindingFailed() {
        return new Result<>(DATA_BINDING_FAILED,"数据绑定失败");
    }

    public static <T> Result<T> unauthorized() {
        return new Result<>(UNAUTHORIZED,"用户未认证");
    }

    public static <T> Result<T> accessDenied() {
        return new Result<>(ACCESS_DENIED,"没有权限访问");
    }

}