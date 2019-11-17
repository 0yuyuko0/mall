package com.yuyuko.mall.stock.api.impl;

import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.idgenerator.SnowflakeIdGenerator;
import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.config.IdGeneratorConfig;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.entity.StockDO;
import com.yuyuko.mall.stock.manager.StockManager;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.stock.service.StockService;
import com.yuyuko.mall.test.autoconfigure.dubbo.DubboTest;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DubboTest
@Import(
        IdGeneratorConfig.class
)
class StockRemotingServiceImplTest {
    @Reference
    private StockRemotingService stockRemotingService;

    @MockBean
    private StockManager stockManager;

    @MockBean
    private StockService stockService;

    @MockBean
    private StockDao stockDao;

    @Test
    void listStocks() {
        when(stockManager.listStocks(anyList())).thenReturn(List.of(100));

        List<Integer> stocks = stockRemotingService.listStocks(List.of(1L));
        assertEquals(1, stocks.size());
    }

    @Test
    void getStock() {
        when(stockManager.getStock(anyLong())).thenReturn(100);

        assertEquals(
                100, stockRemotingService.getStock(1L)
        );
    }

    @Test
    void createProductStock() {
        when(stockDao.insert(any())).thenAnswer(invocation -> {
            StockDO stockDO = invocation.getArgument(0);
            assertTrue(stockDO.getId() > 0);
            assertEquals(1L, stockDO.getProductId());
            assertEquals(100, stockDO.getStock());
            return null;
        });

        stockRemotingService.createProductStock(new StockCreateParam(1L, 100));
    }
}