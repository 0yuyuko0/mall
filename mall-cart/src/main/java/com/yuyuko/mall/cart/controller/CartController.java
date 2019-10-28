package com.yuyuko.mall.cart.controller;

import com.yuyuko.mall.cart.bo.ShopCartItemBO;
import com.yuyuko.mall.cart.param.CartUpdateParam;
import com.yuyuko.mall.cart.service.CartService;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static com.yuyuko.mall.common.result.CommonResult.UNAUTHORIZED;

@RestController
@RequestMapping("/")
@Api(value = "购物车", tags = "Cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("get")
    @ApiOperation(value = "获取购物车", notes = "获取用户的购物车数据，需要登录")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = ShopCartItemBO.class,
                    responseContainer = "List"),
    })
    public Result<?> getCart(@ApiIgnore @SessionAttribute(required = false) UserSessionInfo userSessionInfo) {
        if (userSessionInfo == null)
            return CommonResult.unauthorized();
        return CommonResult.success().data(cartService.getCart(userSessionInfo.getId()));
    }

    @PostMapping("add")
    @ApiOperation(value = "修改购物车item数量", notes = "参数的count为增量，-1代表减1，1代表+1")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
    })
    public Result<?> updateCartItem(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
                                    @RequestBody @Valid CartUpdateParam cartUpdateParam
    ) {
        if (cartUpdateParam.getCount() == 0)
            return CommonResult.success();
        cartService.updateCartItem(userSessionInfo.getId(), cartUpdateParam);
        return CommonResult.success();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除购物车item")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
    })
    public Result<?> deleteCartItem(
            @ApiParam(value = "购物车item的Id,不是购物车中product的id", required = true) @RequestBody Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return CommonResult.success();
    }
}
