package com.yuyuko.mall.shop.api.impl;

import com.yuyuko.mall.shop.api.ShopRemotingService;
import com.yuyuko.mall.shop.config.DataSourceConfig;
import com.yuyuko.mall.shop.config.IdGeneratorConfig;
import com.yuyuko.mall.shop.config.SeataConfig;
import com.yuyuko.mall.shop.dao.ShopDao;
import com.yuyuko.mall.shop.entity.ShopDO;
import com.yuyuko.mall.shop.manager.ShopManager;
import com.yuyuko.mall.test.autoconfigure.dubbo.DubboTest;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
class ShopRemotingServiceGlobalTransactionTest {
    @MockBean
    private ShopManager shopManager;

    @MockBean
    private ShopDao shopDao;

    @Autowired
    private GlobalTransactionOriginator originator;

    @Test
    void createShopGlobalTransactional() {
        Long shopId = originator.createShopGlobalTransactional();
        assertNotNull(shopId);
    }
}