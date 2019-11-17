package com.yuyuko.mall.seller.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.seller.service.SellerService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

@RestController
@RequestMapping("/")
@Api(tags = "Seller")
public class SellerController {
    @Autowired
    SellerService sellerService;

    @GetMapping("home/get")
    @ApiOperation("获取卖家主页信息")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = SellerHomeInfoDTO.class)
    })
    public Result<?> getSellerHomeInfo(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo) {
        return CommonResult.success()
                .data(sellerService.getSellerHomeInfo(userSessionInfo.getId()));
    }

    @PostMapping("register")
    @ApiOperation("使用户注册为卖家")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "")
    })
    public Result<?> registerSeller(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo) {
        return CommonResult.success().data(sellerService.registerSeller(userSessionInfo.getId()));
    }
}
