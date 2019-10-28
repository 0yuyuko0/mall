package com.yuyuko.mall.shop.api.impl;

import com.yuyuko.mall.shop.api.ShopInfoRemotingService;
import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import com.yuyuko.mall.shop.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
@Slf4j
public class ShopInfoRemotingServiceImpl implements ShopInfoRemotingService {
    @Autowired
    private ShopService shopService;

    @Override
    public ShopInfoDTO getShopInfo(Long shopId) {
        return shopService.getShopInfo(shopId);
    }
}
