package com.yuyuko.mall.stock.manager;

import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class StockManager {
    @Autowired
    StockDao stockDao;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RedisExpires redisExpires;

    public Integer getStock(Long productId) {
        String redisKey = getStockRedisKey(productId);
        Integer cache = redisUtils.opsForValue().get(redisKey, Integer.class);
        if (cache != null)
            return cache;
        Integer stock = stockDao.getStock(productId);
        return redisUtils.opsForValue().setAndGet(redisKey, stock, redisExpires);
    }

    private String getStockRedisKey(Long productId) {
        return String.format("prod:%d:stk", productId);
    }

    public List<Integer> listStocks(List<Long> productIds) {
        List<String> redisKeys =
                productIds.stream().map(this::getStockRedisKey).collect(Collectors.toList());
        List<Integer> cache = redisUtils.opsForValue().multiGet(redisKeys, Integer.class);
        CollectionUtils.findAndExecBatchAndReplace(
                cache,
                productIds,
                Objects::isNull,
                stockDao::listStocks,
                (stockDTOs, missProductIds) -> {
                    Map<Long, Integer> m =
                            CollectionUtils.convertListToMap(stockDTOs, "productId", "stock");
                    return missProductIds.stream().map(m::get).collect(Collectors.toList());
                },
                (missProductIds, stocks) -> {
                    List<String> missProductRedisKeys =
                            missProductIds.stream().map(this::getStockRedisKey).collect(Collectors.toList());
                    redisUtils.opsForValue().multiSetIfAbsent(missProductRedisKeys, stocks,
                            Integer.class, redisExpires);
                }
        );
        return cache;
    }
}