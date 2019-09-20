package com.yuyuko.mall.product.controller;


import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.product.service.ProductCategoryService;
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
@RequestMapping("/category/")
public class ProductCategoryController {
    @Autowired
    ProductCategoryService productCategoryService;

    @GetMapping("get")
    public Object getProductCategory(@RequestParam(value = "categoryId",defaultValue = "-1") Long categoryId) {
        return CommonResult.success().data(productCategoryService.getProductCategory(categoryId));
    }

    @GetMapping("brand/get")
    public Object getProductCategoryBrands(@RequestParam("categoryId") Long categoryId) {
        return CommonResult.success().data(productCategoryService.getProductCategoryBrands(categoryId));
    }
}

