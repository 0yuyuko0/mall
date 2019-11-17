package com.yuyuko.mall.stock.api.impl;

import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.config.DataSourceConfig;
import com.yuyuko.mall.stock.config.IdGeneratorConfig;
import com.yuyuko.mall.stock.config.SeataConfig;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.manager.StockManager;
import com.yuyuko.mall.stock.service.StockService;
import com.yuyuko.mall.test.autoconfigure.dubbo.DubboTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DubboTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes =
                GlobalTransactionOriginator.class
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        DataSourceConfig.class,
        SeataConfig.class,
        IdGeneratorConfig.class,
})
class StockRemotingServiceGlobalTransactionTest {
    @Autowired
    private GlobalTransactionOriginator globalTransactionOriginator;

    @MockBean
    private StockService stockService;

    @MockBean
    private StockDao stockDao;

    @MockBean
    private StockManager stockManager;

    @Test
    void test() {
        globalTransactionOriginator.createProductStock();
        verify(stockDao, times(1)).insert(any());
    }
}