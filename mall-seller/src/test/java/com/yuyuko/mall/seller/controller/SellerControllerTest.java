package com.yuyuko.mall.seller.controller;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.seller.service.SellerService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SellerController.class)
class SellerControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SellerService sellerService;

    @Test
    void getSellerHomeInfo() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        when(sellerService.getSellerHomeInfo(1L)).thenReturn(new SellerHomeInfoDTO());
        MvcResult mvcResult = mvc.perform(
                get("/home/get")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();
        Result res = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(),
                Result.class);
        assertEquals(SUCCESS, res.getCode());
        assertNotNull(res.getData());
    }

    @Test
    void registerSeller() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        when(sellerService.registerSeller(1L)).thenReturn(new SellerHomeInfoDTO());
        MvcResult mvcResult = mvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();
        Result res = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(),
                Result.class);
        assertEquals(SUCCESS, res.getCode());
        assertNotNull(res.getData());
    }
}