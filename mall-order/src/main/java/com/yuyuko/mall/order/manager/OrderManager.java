package com.yuyuko.mall.order.manager;

import com.yuyuko.mall.common.utils.CacheUtils;
import com.yuyuko.mall.order.dao.OrderDao;
import com.yuyuko.mall.order.dao.OrderItemDao;
import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.dto.OrderItemDTO;
import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderManager {
    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    RedisUtils redisUtils;

    @Qualifier("orderRedisExpires")
    @Autowired
    RedisExpires redisExpires;

    public OrderDTO getOrder(Long orderId) {
        String redisKey = getOrderRedisKey(orderId);
        OrderDTO cache = redisUtils.opsForValue().get(redisKey, OrderDTO.class);
        return CacheUtils.handleCache(
                cache,
                orderId,
                id -> {
                    OrderDTO order = orderDao.getOrder(orderId);
                    List<OrderItemDTO> orderItems = orderItemDao.listOrderItems(orderId);
                    order.setOrderItems(orderItems);
                    return order;
                },
                order -> order,
                (id, order) -> redisUtils.opsForValue().set(redisKey, order, redisExpires)
        );
    }

    private String getOrderRedisKey(Long orderId) {
        return String.format("order:%d", orderId);
    }
}
