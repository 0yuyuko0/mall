package com.yuyuko.mall.shop.manager;

import com.yuyuko.mall.redis.autoconfigure.RedisUtilsAutoConfiguration;
import com.yuyuko.mall.shop.config.RedisExpiresConfig;
import com.yuyuko.mall.shop.dao.ShopDao;
import com.yuyuko.mall.shop.dto.ShopDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ShopManager.class)
)
@Import({
        RedisUtilsAutoConfiguration.class,
        RedisExpiresConfig.class
})
class ShopManagerTest {
    @Autowired
    private ShopManager shopManager;

    @MockBean
    private ShopDao shopDao;

    @Test
    void getShopInfo() {
        ShopDTO shopDTO = new ShopDTO();

        when(shopDao.getShop(1L)).thenReturn(shopDTO);
        assertEquals(shopDTO, shopManager.getShop(1L));

        shopManager.getShop(1L);

        assertEquals(shopDTO, shopManager.getShop(1L));
        verify(shopDao, times(1)).getShop(1L);
    }
}