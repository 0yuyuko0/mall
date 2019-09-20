package com.yuyuko.mall.cart.controller;

import com.yuyuko.mall.cart.param.CartAddParam;
import com.yuyuko.mall.cart.service.CartService;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("get")
    public Object getCart(@SessionAttribute UserSessionInfo userSessionInfo) {
        return cartService.getCart(userSessionInfo.getId());
    }

    @PostMapping("add")
    public Object addCartItem(@SessionAttribute UserSessionInfo userSessionInfo,
                              @RequestBody @Valid CartAddParam addParam
    ) {
        if (addParam.getCount().intValue() == 0)
            return CommonResult.success();
        cartService.addCartItem(userSessionInfo.getId(), addParam);
        return CommonResult.success();
    }

    @PostMapping("delete")
    public Object deleteCartItem(@RequestBody Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return CommonResult.success();
    }
}
