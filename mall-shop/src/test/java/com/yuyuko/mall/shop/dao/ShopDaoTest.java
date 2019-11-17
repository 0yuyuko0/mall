package com.yuyuko.mall.shop.dao;

import com.yuyuko.mall.shop.entity.ShopDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;

    @BeforeEach
    void insertTestData() {
        ShopDO shopDO = new ShopDO();
        shopDO.setId(-1L);
        shopDO.setName("yuyuko");
        shopDao.insertShop(shopDO);
    }

    @Test
    void getShop() {
        assertEquals("yuyuko", shopDao.getShop(-1L).getName());
    }
}