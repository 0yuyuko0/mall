package com.yuyuko.mall.order.manager;

import com.yuyuko.mall.order.config.RedisExpiresConfig;
import com.yuyuko.mall.order.dao.OrderDao;
import com.yuyuko.mall.order.dao.OrderItemDao;
import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.redis.autoconfigure.RedisUtilsAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataRedisTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = OrderManager.class
))
@Import({
        RedisUtilsAutoConfiguration.class,
        RedisExpiresConfig.class
})
class OrderManagerTest {
    @Autowired
    private OrderManager orderManager;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private OrderItemDao orderItemDao;

    @Test
    void getOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);

        when(orderDao.getOrder(orderDTO.getId())).thenReturn(orderDTO);
        when(orderItemDao.listOrderItems(orderDTO.getId())).thenReturn(List.of());
        assertEquals(orderDTO.getId(), orderManager.getOrder(1L).getId());

        assertEquals(orderDTO.getId(), orderManager.getOrder(1L).getId());
        
        verify(orderDao, times(1)).getOrder(1L);
    }
}