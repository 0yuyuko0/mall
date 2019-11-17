package com.yuyuko.mall.order.dao;

import com.yuyuko.mall.order.entity.OrderDO;
import com.yuyuko.mall.order.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void insertTestData() {
        OrderDO order = new OrderDO();
        order.setId(-1L);
        order.setTimeCreate(LocalDateTime.now());
        order.setDeliveryAddress("");
        order.setConsigneePhoneNumber("");
        order.setConsigneeName("");
        order.setTimeExpectedDelivery("");
        order.setDeliveryMethod("");
        order.setActualPayment(BigDecimal.ZERO);
        order.setDeliveryFee(BigDecimal.ZERO);
        order.setTotalPrice(BigDecimal.ZERO);
        order.setPaymentMethod("");
        order.setStatus(OrderStatus.WAIT_PAY);
        order.setShopId(1L);
        order.setShopName("");
        order.setUserId(1L);
        order.setTimePay(LocalDateTime.now());
        orderDao.insert(order);
    }

    @Test
    void getOrder() {
        assertNotNull(orderDao.getOrder(-1L));
    }

    @Test
    void exist() {
        assertTrue(orderDao.exist(-1L));
        assertFalse(orderDao.exist(-2L));
    }

    @Test
    void checkOrderStatus() {
        assertTrue(orderDao.checkOrderStatus(-1L, OrderStatus.WAIT_PAY));
        assertFalse(orderDao.checkOrderStatus(-1L, OrderStatus.WAIT_SEND));

    }

    @Test
    void updateOrderStatus() {
        assertEquals(1, orderDao.updateOrderStatus(-1L, OrderStatus.WAIT_SEND));
        assertEquals(0, orderDao.updateOrderStatus(-2L, OrderStatus.WAIT_SEND));
    }
}