package com.yuyuko.mall.seller.dao;

import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.seller.dto.SellerShopInfoDTO;
import com.yuyuko.mall.seller.entity.SellerInfoDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.xmlunit.validation.ValidationResult;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellerDaoTest {
    @Autowired
    private SellerDao sellerDao;

    @BeforeEach
    void insertTestData() {
        SellerInfoDO sellerInfoDO = new SellerInfoDO();
        sellerInfoDO.setId(-1L);
        sellerInfoDO.setUserId(-1L);
        sellerInfoDO.setShopId(-1L);
        sellerInfoDO.setShopName("东方店铺");
        sellerDao.insert(sellerInfoDO);
    }

    @Test
    void getSellerHomeInfo() {
        SellerHomeInfoDTO sellerHomeInfo = sellerDao.getSellerHomeInfo(-1L);
        assertEquals(-1L, sellerHomeInfo.getShopId());
        assertEquals("东方店铺", sellerHomeInfo.getShopName());
    }

    @Test
    void getSellerShopInfo() {
        SellerShopInfoDTO sellerShopInfo = sellerDao.getSellerShopInfo(-1L);
        assertEquals(-1L, sellerShopInfo.getShopId());
        assertEquals("东方店铺", sellerShopInfo.getShopName());
    }
}