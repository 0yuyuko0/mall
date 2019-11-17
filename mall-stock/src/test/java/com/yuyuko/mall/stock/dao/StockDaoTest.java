package com.yuyuko.mall.stock.dao;

import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.stock.dto.StockDTO;
import com.yuyuko.mall.stock.entity.StockDO;
import com.yuyuko.mall.stock.param.StockDeductParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StockDaoTest {
    @Autowired
    private StockDao stockDao;

    @BeforeEach
    void insertTestData() {
        StockDO stock = new StockDO();
        stock.setId(-1L);
        stock.setStock(100);
        stock.setProductId(-1L);
        stockDao.insert(stock);
        stock.setId(-2L);
        stock.setProductId(-2L);
        stock.setStock(50);
        stockDao.insert(stock);
    }

    @Test
    void getStock() {
        assertNotNull(stockDao.getStock(-1L));
        assertNotNull(stockDao.getStock(-2L));
        assertNull(stockDao.getStock(-3L));
    }

    @ParameterizedTest
    @MethodSource("listStocksGen")
    void listStocks(List<Long> productIds, int wSize) {
        List<StockDTO> stockDTOS = stockDao.listStocks(productIds);
        assertEquals(wSize, stockDTOS.size());
        if (productIds != null) {
            assertTrue(CollectionUtils.test(productIds, stockDTOS,
                    (id, stockDTO) -> id.equals(stockDTO.getProductId())));
        }
    }

    static Stream<Arguments> listStocksGen() {
        return Stream.of(
                of(null, 0),
                of(List.of(), 0),
                of(List.of(-1L), 1),
                of(List.of(-1L, -2L), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("deductStockGen")
    void deductStock(StockDeductParam deductParam, int wStock) {
        stockDao.deductStock(deductParam);
        assertEquals(wStock, stockDao.getStock(deductParam.getProductId()));
    }

    static Stream<Arguments> deductStockGen() {
        return Stream.of(
                of(new StockDeductParam(-1L, 100), 0),
                of(new StockDeductParam(-1L, 0), 100),
                of(new StockDeductParam(-1L, 50), 50)
        );
    }
}