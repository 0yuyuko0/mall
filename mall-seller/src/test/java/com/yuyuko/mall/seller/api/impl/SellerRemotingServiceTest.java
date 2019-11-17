package com.yuyuko.mall.seller.api.impl;

import com.yuyuko.mall.seller.api.SellerRemotingService;
import com.yuyuko.mall.seller.dao.SellerDao;
import com.yuyuko.mall.seller.dto.SellerShopInfoDTO;
import com.yuyuko.mall.test.autoconfigure.dubbo.DubboTest;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DubboTest
class SellerRemotingServiceTest {
    @Reference
    private SellerRemotingService sellerRemotingService;

    @MockBean
    private SellerDao sellerDao;

    @Test
    void getSellerShopInfo() {
        SellerShopInfoDTO shopInfoDTO = new SellerShopInfoDTO();
        when(sellerDao.getSellerShopInfo(anyLong())).thenReturn(shopInfoDTO);

        assertEquals(shopInfoDTO, sellerRemotingService.getSellerShopInfo(1L));
    }
}