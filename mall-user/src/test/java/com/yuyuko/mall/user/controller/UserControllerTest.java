package com.yuyuko.mall.user.controller;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import com.yuyuko.mall.user.dto.UserHomeInfoDTO;
import com.yuyuko.mall.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void getUserHomeInfo() throws Exception {
        when(userService.getUserHomeInfo(anyLong())).thenReturn(new UserHomeInfoDTO());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo",
                new UserSessionInfo(1L, null, null, null, null));
        //授权
        MvcResult result = mvc.perform(
                get("/home/get")
                        .session(session)
        ).andReturn();

        Result o = JSON.parseObject(result.getResponse().getContentAsByteArray(), Result.class);
        assertEquals(SUCCESS,o.getCode());
        assertNotNull(o.getData());

        //未授权
        result = mvc.perform(
                get("/home/get")
        ).andReturn();
        assertNotEquals(200,result.getResponse().getStatus());
    }

}