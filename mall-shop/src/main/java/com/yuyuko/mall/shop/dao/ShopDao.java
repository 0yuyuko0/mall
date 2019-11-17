package com.yuyuko.mall.shop.dao;

import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.shop.entity.ShopDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopDao {
    ShopDTO getShop(Long shopId);

    int insertShop(ShopDO shopDO);
}