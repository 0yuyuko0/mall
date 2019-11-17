package com.yuyuko.mall.product.manager;

import com.yuyuko.mall.product.config.RedisExpiresConfig;
import com.yuyuko.mall.product.dao.ProductDao;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.redis.autoconfigure.RedisUtilsAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ProductManager.class)
)
@Import({
        RedisUtilsAutoConfiguration.class,
        RedisExpiresConfig.class
})
class ProductManagerTest {

    @Autowired
    private ProductManager productManager;

    @MockBean
    private ProductDao productDao;

    @Test
    void getProduct() {
        ProductDTO productDTO = new ProductDTO();

        when(productDao.getProduct(1L)).thenReturn(productDTO);
        assertEquals(productDTO, productManager.getProduct(1L));

        assertEquals(productDTO, productManager.getProduct(1L));
        verify(productDao, times(1)).getProduct(1L);
    }
}