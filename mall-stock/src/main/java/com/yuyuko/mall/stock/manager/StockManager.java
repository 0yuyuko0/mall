package com.yuyuko.mall.stock.manager;

import com.yuyuko.mall.common.utils.CacheUtils;
import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StockManager {
    @Autowired
    private StockDao stockDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisExpires redisExpires;

    public Integer getStock(Long productId) {
        String redisKey = getStockRedisKey(productId);
        Integer cache = redisUtils.opsForValue().get(redisKey, Integer.class);
        if (cache != null)
            return cache;
        Integer stock = stockDao.getStock(productId);
        return redisUtils.opsForValue().getSet(redisKey, stock, redisExpires);
    }

    private String getStockRedisKey(Long productId) {
        return String.format("prod:%d:stk", productId);
    }

    public List<Integer> listStocks(List<Long> productIds) {
        List<String> redisKeys =
                productIds.stream().map(this::getStockRedisKey).collect(Collectors.toList());
        List<Integer> cache = redisUtils.opsForValue().multiGet(redisKeys, Integer.class);
        return CacheUtils.handleBatchCache(
                cache,
                productIds,
                stockDao::listStocks,
                stockDTOs -> stockDTOs.stream().map(StockDTO::getStock).collect(Collectors.toList()),
                (missProductIds, stocks) -> {
                    List<String> missProductRedisKeys =
                            missProductIds.stream().map(this::getStockRedisKey).collect(Collectors.toList());
                    redisUtils.opsForValue().multiSetIfAbsent(missProductRedisKeys, stocks,
                            redisExpires);
                }
        );
    }
}