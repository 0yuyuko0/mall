package com.yuyuko.mall.order.controller;


import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.order.result.OrderResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.order.param.OrderCreateParam;
import com.yuyuko.mall.order.service.OrderService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static com.yuyuko.mall.common.result.CommonResult.UNAUTHORIZED;
import static com.yuyuko.mall.order.result.OrderResult.*;

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
@Api(tags = "Order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("get")
    @ApiOperation("获取订单信息")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = OrderDTO.class),
    })
    public Result<?> getOrder(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
                              @ApiParam(value = "订单号", required = true)
                              @RequestParam("orderId") @NotNull Long orderId
    ) throws NotOrderOwnerException {
        return CommonResult.success().data(orderService.getOrder(orderId, userSessionInfo.getId()));
    }

    @PostMapping("create")
    @ApiOperation("创建订单")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
    })
    public Result<?> createOrder(
            @ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
            @Valid @RequestBody OrderCreateParam createParam,
            BindingResult bindingResult) {
        orderService.createOrder(userSessionInfo.getId(), userSessionInfo.getNickname(),
                createParam);
        return CommonResult.success();
    }

    @PostMapping("pay")
    @ApiOperation("支付订单")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
            @ApiResponse(code = STOCK_NOT_ENOUGH, message = "库存不足"),
    })
    public Result<?> payOrder(
            @ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
            @ApiParam(required = true) @RequestBody Long orderId
    ) throws WrongOrderStatusException, StockNotEnoughException, NotOrderOwnerException {
        orderService.payOrder(userSessionInfo.getId(), orderId);
        return CommonResult.success();
    }

    @PostMapping("cancel")
    @ApiOperation("取消订单")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
    })
    public Result<?> cancelOrder(
            @ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
            @ApiParam(required = true) @RequestBody Long orderId
    ) throws WrongOrderStatusException, NotOrderOwnerException {
        orderService.cancelOrder(userSessionInfo.getId(), orderId);
        return CommonResult.success();
    }
}

