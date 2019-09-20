package com.yuyuko.mall.product.manager;

import com.yuyuko.mall.product.dao.ProductDao;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope
public class ProductManager {
    @Autowired
    ProductDao productDao;

    @Autowired
    RedisUtils redisUtils;

    @Qualifier("productRedisExpires")
    @Autowired
    RedisExpires redisExpires;

    public ProductDTO getProduct(Long productId) {
        String redisKey = getProductRedisKey(productId);
        ProductDTO cache = redisUtils.opsForValue().get(redisKey, ProductDTO.class);
        if (cache != null)
            return cache;
        ProductDTO productDTO = productDao.getProduct(productId);
        return redisUtils.opsForValue().setAndGet(redisKey, productDTO, redisExpires);
    }

    private String getProductRedisKey(Long productId) {
        return String.format("prod:%d", productId);
    }
}