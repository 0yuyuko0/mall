package com.yuyuko.mall.cart.controller;

import com.yuyuko.mall.cart.bo.ShopCartItemBO;
import com.yuyuko.mall.cart.param.CartAddParam;
import com.yuyuko.mall.cart.param.CartUpdateParam;
import com.yuyuko.mall.cart.service.CartService;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static com.yuyuko.mall.common.result.CommonResult.UNAUTHORIZED;

@RestController
@RequestMapping("/")
@Api(value = "购物车", tags = "Cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("get")
    @ApiOperation(value = "获取购物车", notes = "获取用户的购物车数据，需要登录")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = ShopCartItemBO.class,
                    responseContainer = "List"),
    })
    public Result<?> getCart(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo) {
        return CommonResult.success().data(cartService.getCart(userSessionInfo.getId()));
    }

    @PostMapping("add")
    @ApiOperation(value = "增加购物车item数量（添加商品到购物车）")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
    })
    public Result<?> addCartItem(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestBody @Valid CartAddParam cartAddParam,
                                 BindingResult bindingResult
    ) {
        cartService.addCartItem(userSessionInfo.getId(), cartAddParam);
        return CommonResult.success();
    }

    @PostMapping("update")
    @ApiOperation(value = "修改购物车item数量为count")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
    })
    public Result<?> updateCartItem(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
                                    @RequestBody @Valid CartUpdateParam cartUpdateParam,
                                    BindingResult bindingResult
    ) {
        if (cartUpdateParam.getCount() == 0)
            return CommonResult.success();
        cartService.updateCartItem(userSessionInfo.getId(), cartUpdateParam);
        return CommonResult.success();
    }

    @PostMapping("reduce")
    @ApiOperation(value = "购物车item数量减1")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = ""),
    })
    public Result<?> reduceCartItem(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
                                    @RequestBody @ApiParam("购物车itemId") @NotNull Long id,
                                    BindingResult bindingResult
    ) {
        cartService.reduceCartItem(userSessionInfo.getId(), id);
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
