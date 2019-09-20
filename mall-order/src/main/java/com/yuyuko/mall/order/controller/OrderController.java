package com.yuyuko.mall.order.controller;


import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.order.result.OrderResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.order.param.OrderCreateParam;
import com.yuyuko.mall.order.service.OrderService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-13
 */
@RestController
@RequestMapping("/")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("get")
    public Result<?> getOrder(@SessionAttribute UserSessionInfo userSessionInfo,
                              @RequestParam("orderId") @NotNull Long orderId
    ) {
        return CommonResult.success().data(orderService.getOrder(orderId));
    }

    @PostMapping("create")
    public Result<?> createOrder(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @Valid @RequestBody OrderCreateParam createParam,
            BindingResult bindingResult) {
        orderService.createOrder(userSessionInfo.getId(), userSessionInfo.getNickname(), createParam);
        return CommonResult.success();
    }

    @PostMapping("pay")
    public Result<?> payOrder(
            @SessionAttribute UserSessionInfo userSessionInfo,
            @RequestBody Long orderId
    ) {
        try {
            orderService.payOrder(1L,orderId);
        } catch (WrongOrderStatusException e) {
            return OrderResult.wrongOrderStatus();
        } catch (StockNotEnoughException e) {
            return OrderResult.stockNotEnough();
        } catch (NotOrderOwnerException e) {
            return OrderResult.notOrderOwner();
        }
        return CommonResult.success();
    }
}

