package com.yuyuko.mall.order.manager;

import com.yuyuko.mall.order.dao.OrderDao;
import com.yuyuko.mall.order.dao.OrderItemDao;
import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.dto.OrderItemDTO;
import com.yuyuko.mall.order.entity.OrderDO;
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
        if (cache != null)
            return cache;
        return getOrderFromDBAndPutToCache(redisKey, orderId);
    }

    private OrderDTO getOrderFromDBAndPutToCache(String redisKey, Long orderId) {
        OrderDTO order = orderDao.getOrder(orderId);
        List<OrderItemDTO> orderItems = orderItemDao.listOrderItems(orderId);
        order.setOrderItems(orderItems);
        redisUtils.opsForValue().set(redisKey, order, redisExpires);
        return order;
    }

    private String getOrderRedisKey(Long orderId) {
        return String.format("order:%d", orderId);
    }
}
