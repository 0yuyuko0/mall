package com.yuyuko.mall.shop.service;

import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.shop.manager.ShopManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    @Autowired
    private ShopManager shopManager;

    public ShopDTO getShop(Long shopId){
        return shopManager.getShop(shopId);
    }
}
