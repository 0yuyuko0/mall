package com.yuyuko.mall.product.manager;

import com.yuyuko.mall.common.utils.CacheUtils;
import com.yuyuko.mall.product.dao.ProductDao;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ProductManager {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private RedisUtils redisUtils;

    @Qualifier("productRedisExpires")
    @Autowired
    private RedisExpires redisExpires;

    public ProductDTO getProduct(Long productId) {
        String redisKey = getProductRedisKey(productId);
        ProductDTO cache = redisUtils.opsForValue().get(redisKey, ProductDTO.class);
        return redisUtils.opsForValue().getSet(redisKey,
                CacheUtils.handleCache(
                        cache,
                        productId,
                        productDao::getProduct,
                        p -> p,
                        null
                ), redisExpires);
    }

    private String getProductRedisKey(Long productId) {
        return String.format("prod:%d", productId);
    }
}