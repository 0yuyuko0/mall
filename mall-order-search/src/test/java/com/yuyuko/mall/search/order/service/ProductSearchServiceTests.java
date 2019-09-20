package com.yuyuko.mall.search.order.service;

import com.yuyuko.mall.search.order.entity.Order;
import com.yuyuko.mall.search.order.param.OrderSearchParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSearchServiceTests {

    @Autowired
    OrderSearchService orderSearchService;

    @Test
    public void searchOrders() {
        OrderSearchParam orderSearchParam = new OrderSearchParam();
        Page<Order> orders = orderSearchService.searchOrders(1L, orderSearchParam);
        System.out.println(orders);
    }
}