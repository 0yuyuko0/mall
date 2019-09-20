package com.yuyuko.mall.cart.controller.advice;

import com.yuyuko.mall.cart.controller.CartController;
import com.yuyuko.mall.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = CartController.class)
@Slf4j
public class CartControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public Object handle(RuntimeException ex) {
        log.error(ex.getMessage());
        return CommonResult.failed();
    }
}
