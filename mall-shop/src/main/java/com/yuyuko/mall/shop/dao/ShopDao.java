package com.yuyuko.mall.shop.dao;

import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import com.yuyuko.mall.shop.entity.ShopDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDao {
    ShopInfoDTO getShop(Long shopId);
}