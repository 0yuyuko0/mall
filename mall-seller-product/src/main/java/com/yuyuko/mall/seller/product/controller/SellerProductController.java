package com.yuyuko.mall.seller.product.controller;


import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.seller.product.param.ProductPublishParam;
import com.yuyuko.mall.seller.product.service.SellerProductService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
public class SellerProductController {
    @Autowired
    SellerProductService sellerProductService;

    @PostMapping("publish")
    public Object publishProduct(@SessionAttribute UserSessionInfo userSessionInfo,
                                 @RequestBody @Valid ProductPublishParam publishParam,
                                 BindingResult bindingResult) {
        sellerProductService.publishProduct(userSessionInfo.getId(), publishParam);
        return CommonResult.success();
    }
}

