package com.yuyuko.mall.shop.api.impl;

import com.yuyuko.mall.shop.api.ShopRemotingService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class GlobalTransactionOriginator {
    @Reference
    private ShopRemotingService shopRemotingService;

    @GlobalTransactional
    public Long createShopGlobalTransactional() {
        return shopRemotingService.createShop("yuyuko");
    }
}
