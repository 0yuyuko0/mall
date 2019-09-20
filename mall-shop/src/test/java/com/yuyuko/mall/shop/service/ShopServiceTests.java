package com.yuyuko.mall.shop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShopServiceTests {
    @Autowired
    ShopService shopService;

    @Test
    public void getShopInfo() {
        System.out.println(shopService.getShopInfo(1L));
    }
}