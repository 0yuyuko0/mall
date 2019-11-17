package com.yuyuko.mall.shop.controller;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.shop.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ShopController.class)
class ShopControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopService shopService;

    @Test
    void getShop() throws Exception {
        when(shopService.getShop(1L)).thenReturn(new ShopDTO());
        MvcResult result = mockMvc.perform(
                get("/get")
                        .param("shopId", "1")
        ).andReturn();
        Result res = JSON.parseObject(result.getResponse().getContentAsByteArray(), Result.class);
        assertEquals(SUCCESS, res.getCode());
        assertNotNull(res.getData());
    }
}