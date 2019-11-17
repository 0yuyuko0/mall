package com.yuyuko.mall.user.controller.advice;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.user.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        UserController.class,
})
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(Throwable.class)
    private Result<?> handle(Throwable ex) {
        log.error("Throwable", ex);
        return CommonResult.failed();
    }
}
