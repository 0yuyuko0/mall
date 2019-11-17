package com.yuyuko.mall.product.controller;


import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.product.bo.ProductBO;
import com.yuyuko.mall.product.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-12
 */
@RestController
@RequestMapping("/")
@Api(value = "商品", tags = "Product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("get")
    @ApiOperation(value = "获取商品")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = ProductBO.class)
    })
    public Result<?> getProduct(
            @ApiParam(name = "productId", required = true)
            @RequestParam("productId") Long productId) {
        return CommonResult.success().data(productService.getProduct(productId));
    }
}

