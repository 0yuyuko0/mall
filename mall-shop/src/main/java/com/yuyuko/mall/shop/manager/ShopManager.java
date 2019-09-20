package com.yuyuko.mall.shop.manager;

import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import com.yuyuko.mall.shop.dao.ShopDao;
import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class ShopManager {
    @Autowired
    ShopDao shopDao;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RedisExpires redisExpires;

    public ShopInfoDTO getShopInfo(Long shopId) {
        String redisKey = getShopInfoRedisKey(shopId);
        ShopInfoDTO cache = redisUtils.opsForValue().get(redisKey, ShopInfoDTO.class);
        if (cache != null)
            return cache;
        ShopInfoDTO shop = shopDao.getShop(shopId);
        redisUtils.opsForValue().set(redisKey, shop, redisExpires);
        return shop;
    }

    private String getShopInfoRedisKey(Long shopId) {
        return String.format("shop:%d", shopId);
    }
}
