package com.yuyuko.mall.shop.service;

import com.yuyuko.mall.shop.dao.ShopDao;
import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import com.yuyuko.mall.shop.manager.ShopManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    @Autowired
    ShopManager shopManager;

    public ShopInfoDTO getShopInfo(Long shopId){
        return shopManager.getShopInfo(shopId);
    }
}
