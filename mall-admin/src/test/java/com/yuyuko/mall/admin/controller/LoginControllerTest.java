package com.yuyuko.mall.admin.controller;

import com.alibaba.csp.sentinel.command.CommandRequest;
import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.admin.exception.IncorrectUsernameOrPasswordException;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.exception.UsernameNotExistsException;
import com.yuyuko.mall.admin.param.LoginParam;
import com.yuyuko.mall.admin.result.UserResult;
import com.yuyuko.mall.admin.service.UserService;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.yuyuko.mall.admin.result.UserResult.*;
import static com.yuyuko.mall.common.result.CommonResult.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @ParameterizedTest
    @MethodSource("loginGen")
    void login(LoginParam loginParam, Class<? extends Throwable> wEx, int wCode) throws Exception {
        beforeLogin(wEx);
        MvcResult mvcResult = mvc.perform(
                post("/login")
                        .content(JSON.toJSONString(loginParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        Result res = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(),
                Result.class);
        assertEquals(wCode, res.getCode());
    }

    void beforeLogin(Class<? extends Throwable> throwable) throws Exception {
        if (throwable != null)
            when(userService.login(any())).thenThrow(throwable);
        else
            when(userService.login(any())).thenReturn(new UserSessionInfo());
    }

    static Stream<Arguments> loginGen() {
        return Stream.of(
                of(new LoginParam("123", "456"), null, INCORRECT_USERNAME_OR_PASSWORD),
                of(new LoginParam("1236781233", "456"), null, INCORRECT_USERNAME_OR_PASSWORD),
                of(new LoginParam("1236781233", "456123123"), UsernameNotExistsException.class,
                        USERNAME_NOT_EXISTS),
                of(new LoginParam("1236781233", "456123123"),
                        IncorrectUsernameOrPasswordException.class,
                        INCORRECT_USERNAME_OR_PASSWORD),
                of(new LoginParam("1236781233", "456123123"), null, SUCCESS)
        );
    }


    @ParameterizedTest
    @MethodSource("registerGen")
    void register(LoginParam loginParam, Class<? extends Throwable> wEx, int wCode) throws Exception {
        beforeRegister(wEx);
        MvcResult mvcResult = mvc.perform(
                post("/register")
                        .content(JSON.toJSONString(loginParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        Result res = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(),
                Result.class);
        assertEquals(wCode, res.getCode());
    }

    void beforeRegister(Class<? extends Throwable> throwable) throws Exception {
        if (throwable != null)
            doThrow(throwable).when(userService).register(any());
    }

    static Stream<Arguments> registerGen() {
        return Stream.of(
                of(new LoginParam("123", "456"), null, INCORRECT_USERNAME_OR_PASSWORD),
                of(new LoginParam("1236781233", "456"), null, INCORRECT_USERNAME_OR_PASSWORD),
                of(new LoginParam("1236781233", "456123123"),
                        UsernameAlreadyExistException.class,
                        USERNAME_ALREADY_EXISTS),
                of(new LoginParam("1236781233", "456123123"), null, SUCCESS)
        );
    }

    @Test
    void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(
                post("/logout")
                        .session(session)
        ).andReturn();
        assertTrue(session.isInvalid());
    }

}