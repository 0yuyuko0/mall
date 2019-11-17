package com.yuyuko.mall.shop.api.impl;

import com.yuyuko.mall.shop.api.ShopRemotingService;
import com.yuyuko.mall.shop.config.IdGeneratorConfig;
import com.yuyuko.mall.shop.dao.ShopDao;
import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.shop.entity.ShopDO;
import com.yuyuko.mall.shop.manager.ShopManager;
import com.yuyuko.mall.test.autoconfigure.dubbo.DubboTest;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DubboTest
@Import(IdGeneratorConfig.class)
class ShopRemotingServiceTest {
    @Reference
    private ShopRemotingService shopRemotingService;

    @MockBean
    private ShopManager shopManager;

    @MockBean
    private ShopDao shopDao;

    @Test
    void getShopInfo() {
        ShopDTO shopDTO = new ShopDTO();
        when(shopManager.getShop(anyLong())).thenReturn(shopDTO);
        assertEquals(shopDTO, shopRemotingService.getShopInfo(1L));
    }

    @Test
    void createShop() {
        when(shopDao.insertShop(any())).thenAnswer(invocation -> {
            ShopDO shopDO = invocation.getArgument(0);
            assertEquals("yuyuko", shopDO.getName());
            assertNotNull(shopDO.getId());
            return 1 ;
        });
        shopRemotingService.createShop("yuyuko");
    }
}