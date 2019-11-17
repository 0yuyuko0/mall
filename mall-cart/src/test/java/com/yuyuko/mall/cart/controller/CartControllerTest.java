package com.yuyuko.mall.cart.controller;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.cart.param.CartAddParam;
import com.yuyuko.mall.cart.param.CartUpdateParam;
import com.yuyuko.mall.cart.service.CartService;
import com.yuyuko.mall.common.result.Result;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CartController.class)
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void getCart() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        MvcResult result = mockMvc.perform(
                get("/get")
                        .session(session)
        ).andReturn();

        assertEquals(SUCCESS, ((Result) JSON.parseObject(result.getResponse().getContentAsString(),
                Result.class)).getCode());
    }

    @Test
    void addCartItem() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        MvcResult result = mockMvc.perform(
                post("/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
                        .content(JSON.toJSONString(new CartAddParam().setCount(1).setProductId(1L))
                        )).andReturn();
        assertEquals(SUCCESS, ((Result) JSON.parseObject(result.getResponse().getContentAsString(),
                Result.class)).getCode());
        result = mockMvc.perform(
                post("/add")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
                        .content(JSON.toJSONString(
                                new CartAddParam()
                                        .setShopId(1L)
                                        .setCount(1)
                                        .setShopName("")
                                        .setProductId(1L)
                                )
                        )).andReturn();
        assertEquals(SUCCESS, ((Result) JSON.parseObject(result.getResponse().getContentAsString(),
                Result.class)).getCode());
    }

    @Test
    void updateCartItem() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        MvcResult result = mockMvc.perform(
                post("/update")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
                        .content(JSON.toJSONString(new CartUpdateParam().setCount(1).setId(1L))
                        )).andReturn();
        assertEquals(SUCCESS, ((Result) JSON.parseObject(result.getResponse().getContentAsString(),
                Result.class)).getCode());

    }

    @Test
    void reduceCartItem() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        MvcResult result = mockMvc.perform(
                post("/reduce")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
                        .content(JSON.toJSONString(1L)
                        )).andReturn();
        assertEquals(SUCCESS, ((Result) JSON.parseObject(result.getResponse().getContentAsString(),
                Result.class)).getCode());
    }

    @Test
    void deleteCartItem() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        MvcResult result = mockMvc.perform(
                post("/delete")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
                        .content(JSON.toJSONString(1L)
                        )).andReturn();
        assertEquals(SUCCESS, ((Result) JSON.parseObject(result.getResponse().getContentAsString(),
                Result.class)).getCode());
    }
}