package com.yuyuko.mall.seller.service;

import com.yuyuko.mall.seller.config.DataSourceConfig;
import com.yuyuko.mall.seller.config.IdGeneratorConfig;
import com.yuyuko.mall.seller.config.SeataConfig;
import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.shop.api.ShopRemotingService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@MybatisTest(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        SellerService.class,
                })
        }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        DataSourceConfig.class,
        SeataConfig.class,
        IdGeneratorConfig.class,
})
class SellerServiceRegisterTest {
    @Autowired
    private SellerService sellerService;

    @MockBean
    private ShopRemotingService shopRemotingService;

    @Test
    void registerSeller() {
        when(shopRemotingService.createShop(anyString())).thenReturn(-1L);
        ReflectionTestUtils.setField(sellerService, "shopRemotingService", shopRemotingService);

        SellerHomeInfoDTO sellerHomeInfo1 = sellerService.registerSeller(-1L);
        assertEquals(-1L, sellerHomeInfo1.getShopId());
        assertEquals(16, sellerHomeInfo1.getShopName().length());

        SellerHomeInfoDTO sellerHomeInfo2 = sellerService.getSellerHomeInfo(-1L);

        assertEquals(sellerHomeInfo2, sellerHomeInfo1);
    }
}