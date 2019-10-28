package com.yuyuko.mall.search.product.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.search.product.entity.Product;
import com.yuyuko.mall.search.product.param.ProductSearchParam;
import com.yuyuko.mall.search.product.service.ProductSearchService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

@RestController
@RequestMapping("/")
@Api(value = "搜索商品", tags = "SearchProduct")
public class ProductSearchController {
    @Autowired
    ProductSearchService productSearchService;

    @ApiModel(description = "搜索返回结果，content字段是结果数组，其余几个schema不重要")
    static class SearchResponse extends PageImpl<Product> {
        public SearchResponse(List<Product> content) {
            super(content);
        }
    }

    @GetMapping
    @ApiOperation("搜索商品")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = SearchResponse.class)
    })
    public Result<?> searchProducts(@ApiParam @ModelAttribute @Valid ProductSearchParam productSearchParam) {
        return CommonResult.success().data(productSearchService.searchProducts(productSearchParam));
    }
}
