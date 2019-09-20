package com.yuyuko.mall.shop.api.impl;

import com.yuyuko.mall.common.exception.WrappedException;
import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import com.yuyuko.mall.shop.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
@Slf4j
public class ShopInfoService implements com.yuyuko.mall.shop.api.ShopInfoService {
    @Autowired
    ShopService shopService;

    @Override
    public ShopInfoDTO getShopInfo(Long shopId) {
        try {
            return shopService.getShopInfo(shopId);
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            throw new WrappedException(ex);
        }
    }
}
