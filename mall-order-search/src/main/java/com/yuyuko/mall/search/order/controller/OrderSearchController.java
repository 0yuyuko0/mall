package com.yuyuko.mall.search.order.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.search.order.param.OrderSearchParam;
import com.yuyuko.mall.search.order.service.OrderSearchService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class OrderSearchController {
    @Autowired
    OrderSearchService orderSearchService;

    @GetMapping
    public Object searchOrders(@ModelAttribute @Valid OrderSearchParam orderSearchParam,
                               @SessionAttribute UserSessionInfo userSessionInfo
    ) {
        return CommonResult.success().data(orderSearchService.searchOrders(userSessionInfo.getId(),orderSearchParam));
    }
}
