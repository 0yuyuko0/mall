package com.yuyuko.mall.order.controller.advice;

import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.order.controller.OrderController;
import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.order.result.OrderResult;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = OrderController.class)
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
    private Result<?> handle(StockNotEnoughException  ex){
        return OrderResult.stockNotEnough();
    }
}
