package com.yuyuko.mall.admin.controller.advice;

import com.yuyuko.mall.admin.controller.LoginController;
import com.yuyuko.mall.admin.exception.IncorrectUsernameOrPasswordException;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.exception.UsernameNotExistsException;
import com.yuyuko.mall.admin.result.UserResult;
import com.yuyuko.mall.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = LoginController.class)
@Slf4j
public class LoginControllerAdvice {
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public Object handle(UsernameAlreadyExistException ex) {
        return UserResult.usernameAlreadyExists();
    }

    @ExceptionHandler(IncorrectUsernameOrPasswordException.class)
    public Object handle(IncorrectUsernameOrPasswordException ex) {
        return UserResult.incorrectUsernameOrPassword();
    }

    @ExceptionHandler(UsernameNotExistsException.class)
    public Object handle(UsernameNotExistsException ex) {
        return UserResult.usernameNotExists();
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handle(RuntimeException ex) {
        log.error(ex.getMessage());
        return CommonResult.failed();
    }
}