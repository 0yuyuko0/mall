package com.yuyuko.mall.product.controller;


import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("get")
    public Object getProduct(@RequestParam("productId") Long productId) {
        return CommonResult.success().data(productService.getProduct(productId));
    }
}

