package com.yuyuko.mall.stock.manager;

import com.yuyuko.mall.redis.autoconfigure.RedisUtilsAutoConfiguration;
import com.yuyuko.mall.stock.config.RedisExpiresConfig;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.dto.StockDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataRedisTest(
        includeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StockManager.class)
)
@Import({
        RedisUtilsAutoConfiguration.class,
        RedisExpiresConfig.class
})
class StockManagerTest {
    @Autowired
    private StockManager stockManager;

    @MockBean
    private StockDao stockDao;

    @Test
    void getStock() {
        when(stockDao.getStock(-1L)).thenReturn(100);

        assertEquals(100, stockManager.getStock(-1L));

        assertEquals(100, stockManager.getStock(-1L));

        verify(stockDao, times(1)).getStock(-1L);
    }

    @Test
    void listStocks() {
        when(stockDao.listStocks(List.of(1L, 2L))).thenReturn(
                List.of(new StockDTO(1L, 100), new StockDTO(2L, 200))
        );

        assertArrayEquals(new Integer[]{100, 200},
                stockManager.listStocks(List.of(1L, 2L)).toArray(Integer[]::new));

        verify(stockDao, times(1)).listStocks(anyList());

        assertArrayEquals(new Integer[]{100, 200},
                stockManager.listStocks(List.of(1L, 2L)).toArray(Integer[]::new));

        verify(stockDao, times(1)).listStocks(List.of(1L, 2L));

        reset(stockDao);

        when(stockDao.listStocks(List.of(3L))).thenReturn(
                List.of(new StockDTO(3L, 300))
        );

        assertArrayEquals(new Integer[]{100, 200, 300},
                stockManager.listStocks(List.of(1L, 2L, 3L)).toArray(Integer[]::new));

        verify(stockDao, times(1)).listStocks(List.of(3L));
    }
}