package com.yuyuko.mall.seller.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.seller.service.SellerInfoService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import springfox.documentation.annotations.ApiIgnore;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

@RestController
@RequestMapping("/")
@Api(tags = "Seller")
public class SellerController {
    @Autowired
    SellerInfoService sellerInfoService;

    @GetMapping("home/get")
    @ApiOperation("获取卖家主页信息")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = SellerHomeInfoDTO.class)
    })
    public Object getSellerHomeInfo(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo) {
        return CommonResult.success().data(sellerInfoService.getSellerHomeInfo(userSessionInfo.getId()));
    }
}
