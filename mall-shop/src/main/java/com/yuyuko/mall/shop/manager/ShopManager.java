package com.yuyuko.mall.shop.manager;

import com.yuyuko.mall.common.utils.CacheUtils;
import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import com.yuyuko.mall.shop.dao.ShopDao;
import com.yuyuko.mall.shop.dto.ShopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
public class ShopManager {
    @Autowired
    private ShopDao shopDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisExpires redisExpires;

    public ShopDTO getShop(Long shopId) {
        String redisKey = getShopInfoRedisKey(shopId);
        ShopDTO cache = redisUtils.opsForValue().get(redisKey, ShopDTO.class);
        return redisUtils.opsForValue().getSet(redisKey,
                CacheUtils.handleCache(
                        cache,
                        shopId,
                        shopDao::getShop,
                        p -> p,
                        null
                ), redisExpires);
    }

    private String getShopInfoRedisKey(Long shopId) {
        return String.format("shop:%d", shopId);
    }
}
