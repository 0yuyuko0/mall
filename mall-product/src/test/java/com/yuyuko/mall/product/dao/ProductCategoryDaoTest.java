package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.idgenerator.SnowflakeIdGenerator;
import com.yuyuko.mall.product.entity.ProductCategoryDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductCategoryDaoTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;


    @BeforeEach
    public void insertTestData() {
        ProductCategoryDO productCategory = new ProductCategoryDO();
        productCategory.setId(-10L);
        productCategory.setParentId(-1L);
        productCategory.setName("手机数码");
        productCategory.setLevel(1);
        productCategory.setIsLeaf(false);

        productCategoryDao.insert(productCategory);
        productCategory.setParentId(-10L);
        productCategory.setId(-11L);
        productCategory.setName("手机");
        productCategory.setLevel(2);

        productCategoryDao.insert(productCategory);
    }

    @ParameterizedTest
    @MethodSource("listChildCategoriesGen")
    void listChildCategories(long parentId, int wSize) {
        assertEquals(wSize,
                productCategoryDao.listChildCategories(parentId).size());
    }

    static Stream<Arguments> listChildCategoriesGen() {
        return Stream.of(
                of(-1, 1),
                of(-2L, 0),
                of(-10L, 1)
        );
    }
}