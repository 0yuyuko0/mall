package com.yuyuko.mall.order.controller.advice;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.order.controller.OrderController;
import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.order.result.OrderResult;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = OrderController.class)
@Slf4j
public class OrderControllerAdvice {
    @ExceptionHandler(WrongOrderStatusException.class)
    private Result<?> handle(WrongOrderStatusException ex) {
        return OrderResult.wrongOrderStatus();
    }

    @ExceptionHandler(NotOrderOwnerException.class)
    private Result<?> handle(NotOrderOwnerException ex) {
        return OrderResult.notOrderOwner();
    }

    @ExceptionHandler(StockNotEnoughException.class)
    private Result<?> handle(StockNotEnoughException ex) {
        return OrderResult.stockNotEnough();
    }

    @ExceptionHandler(Throwable.class)
    private Result<?> handle(Throwable ex) {
        log.error("", ex);
        return CommonResult.failed();
    }
}
