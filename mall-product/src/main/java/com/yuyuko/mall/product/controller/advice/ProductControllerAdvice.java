package com.yuyuko.mall.product.controller.advice;

import com.yuyuko.mall.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.yuyuko.mall.product.controller")
@Slf4j
public class ProductControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public Object handle(RuntimeException ex) {
        log.error(ex.getMessage());
        return CommonResult.failed();
    }
}
