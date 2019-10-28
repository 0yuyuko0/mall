package com.yuyuko.mall.seller.product.controller;


import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.seller.product.param.ProductPublishParam;
import com.yuyuko.mall.seller.product.service.SellerProductService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-09
 */
@RestController
@RequestMapping("/")
@Api(tags = "SellerProduct")
public class SellerProductController {
    @Autowired
    SellerProductService sellerProductService;

    @PostMapping("publish")
    @ApiOperation("发布商品")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "")
    })
    public Object publishProduct(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestBody @Valid ProductPublishParam publishParam,
                                 BindingResult bindingResult) {
        sellerProductService.publishProduct(userSessionInfo.getId(), publishParam);
        return CommonResult.success();
    }
}

