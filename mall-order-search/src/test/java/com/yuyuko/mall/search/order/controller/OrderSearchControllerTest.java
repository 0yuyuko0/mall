package com.yuyuko.mall.search.order.controller;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.search.order.service.OrderSearchService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(OrderSearchController.class)
class OrderSearchControllerTest {
    @MockBean
    private OrderSearchService orderSearchService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchOrders() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        MvcResult result = mockMvc.perform(
                get("/")
                        .session(session)
                        .param("keyword", "123")
        ).andReturn();
        assertEquals(SUCCESS,
                ((Result) JSON.parseObject(result.getResponse().getContentAsByteArray(),
                        Result.class)).getCode());
    }
}