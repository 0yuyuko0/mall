package com.yuyuko.mall.search.product.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.search.product.param.ProductSearchParam;
import com.yuyuko.mall.search.product.service.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class ProductSearchController {
    @Autowired
    ProductSearchService productSearchService;

    @GetMapping
    public Object searchProducts(@ModelAttribute @Valid ProductSearchParam productSearchParam) {
        return CommonResult.success().data(productSearchService.searchProducts(productSearchParam));
    }
}
