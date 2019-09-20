package com.yuyuko.mall.admin.result;

import com.yuyuko.mall.common.result.Result;

public class UserResult {

    public static final int INCORRECT_USERNAME_OR_PASSWORD = 601;

    public static final int USERNAME_ALREADY_EXISTS = 602;

    public static final int USERNAME_NOT_EXISTS = 603;

    public static Result incorrectUsernameOrPassword() {
        return new Result(INCORRECT_USERNAME_OR_PASSWORD, "用户名或密码不正确");
    }

    public static Result usernameAlreadyExists() {
        return new Result(USERNAME_ALREADY_EXISTS, "用户名已存在");
    }

    public static Result usernameNotExists() {
        return new Result(USERNAME_NOT_EXISTS, "该用户名没有注册过");
    }
}
