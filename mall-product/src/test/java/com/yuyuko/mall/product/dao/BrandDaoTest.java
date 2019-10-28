package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.product.entity.BrandDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandDaoTest {
    @Autowired
    private BrandDao brandDao;

    @BeforeEach
    public void insertTestData() {
        BrandDO brand = new BrandDO();
        brand.setId(-1L);
        brand.setName("小米");
        brand.setAlias("MI");

        brandDao.insert(brand);

        brand.setId(-2L);
        brand.setName("华为");
        brand.setAlias("HUAWEI");

        brandDao.insert(brand);

        brandDao.insertProductCategoryBrandRelation(-1L, -2L, -1L);
        brandDao.insertProductCategoryBrandRelation(-2L, -2L, -2L);
    }

    @Test
    void listBrandsOfCategory() {
        assertEquals(2, brandDao.listBrandsOfCategory(-2L).size());
        assertEquals(0, brandDao.listBrandsOfCategory(-3L).size());
    }
}