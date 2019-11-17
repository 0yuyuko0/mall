package com.yuyuko.mall.shop.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.shop.service.ShopService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

@RestController
@RequestMapping("/")
@Api(value = "店铺",tags = "Shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @GetMapping("get")
    @ApiOperation(value = "获取店铺信息")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = ShopDTO.class)
    })
    public Result<?> getShop(
            @ApiParam(name = "shopId", required = true)
            @RequestParam("shopId") Long shopId) {
        return CommonResult.success().data(shopService.getShop(shopId));
    }
}
