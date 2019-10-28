package com.yuyuko.mall.product.controller;


import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.product.dto.BrandDTO;
import com.yuyuko.mall.product.dto.ProductCategoryDTO;
import com.yuyuko.mall.product.service.ProductCategoryService;
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
@RequestMapping("/category/")
@Api(value = "商品类目", tags = "ProductCategory")
public class ProductCategoryController {
    @Autowired
    ProductCategoryService productCategoryService;

    @GetMapping("get")
    @ApiOperation(value = "获取商品类目的子类目列表", notes = "根据父类目id获取其子类目list，如果要获取顶级类目List则传入-1")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = ProductCategoryDTO.class,
                    responseContainer = "List")
    })
    public Object listChildCategories(
            @ApiParam(name = "parentId", required = true, value = "父类目id")
            @RequestParam("parentId") Long parent) {
        return CommonResult.success().data(productCategoryService.listChildCategories(parent));
    }

    @GetMapping("brand/get")
    @ApiOperation(value = "获取商品类目下的所有品牌", notes = "根据商品类目id获取该类目下的所有品牌，类目必须是类目树的叶子类目")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = BrandDTO.class,
                    responseContainer = "List")
    })
    public Object listBrandsOfCategory(
            @ApiParam(name = "categoryId", required = true, value = "叶子类目id")
            @RequestParam("categoryId") Long categoryId) {
        return CommonResult.success().data(productCategoryService.listBrandsOfCategory(categoryId));
    }
}
