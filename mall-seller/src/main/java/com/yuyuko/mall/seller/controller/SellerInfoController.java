package com.yuyuko.mall.seller.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.seller.service.SellerInfoService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("info")
public class SellerInfoController {
    @Autowired
    SellerInfoService sellerInfoService;

    @GetMapping("get")
    public Object getSellerInfo(@SessionAttribute UserSessionInfo userSessionInfo) {
        return CommonResult.success().data(sellerInfoService.getSellerInfo(userSessionInfo.getId()));
    }
}
