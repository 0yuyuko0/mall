package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.product.entity.ProductDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductDaoTest {
    @Autowired
    private ProductDao productDao;

    @BeforeEach
    public void insertTestData() {
        ProductDO productDO = new ProductDO();
        productDO.setId(-1L);
        productDO.setSellerId(1L);
        productDO.setShopId(1L);
        productDO.setBrandId(1L);
        productDO.setCategoryId(1L);
        productDO.setName("小米5");
        productDO.setPrice(BigDecimal.valueOf(1999));
        productDO.setAvatar("");
        productDao.insert(productDO);

        productDO.setId(-2L);
        productDO.setName("小米6");
        productDao.insert(productDO);
    }

    @Test
    void getProduct() {
        assertNotNull(productDao.getProduct(-1L));
        assertNotNull(productDao.getProduct(-2L));
        assertNull(productDao.getProduct(-3L));
    }

    @ParameterizedTest
    @MethodSource("listCartItemProductsGen")
    void listCartItemProducts(List<Long> productIds, int wSize) {
        List<CartItemProductDTO> cartItemProducts = productDao.listCartItemProducts(productIds);
        assertEquals(wSize, cartItemProducts.size());
        if (productIds != null && productIds.size() == cartItemProducts.size())
            assertTrue(CollectionUtils.test(productIds, cartItemProducts,
                    (productId, cartItemProduct) -> productId.equals(cartItemProduct.getId())));
    }

    static Stream<Arguments> listCartItemProductsGen() {
        return Stream.of(
                of(null, 0),
                of(List.of(), 0),
                of(List.of(0L), 0),
                of(List.of(-4L, -3L), 0),
                of(List.of(-1L), 1),
                of(List.of(-1L, -3L), 1),
                of(List.of(-1L, -2L), 2)
        );
    }
}