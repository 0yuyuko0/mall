package com.yuyuko.mall.seller.product.controller;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.seller.product.aspect.BindingResultAspect;
import com.yuyuko.mall.seller.product.param.ProductPublishParam;
import com.yuyuko.mall.seller.product.service.SellerProductService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.yuyuko.mall.common.result.CommonResult.DATA_BINDING_FAILED;
import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(value = SellerProductController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = BindingResultAspect.class
        )
)
@ImportAutoConfiguration(AopAutoConfiguration.class)
class SellerProductControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SellerProductService sellerProductService;

    @Test
    void login() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        ProductPublishParam param = new ProductPublishParam();
        //未授权
        MvcResult mvcResult = mvc.perform(
                post("/publish")
                        .content(JSON.toJSONString(param))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        assertNotEquals(200, mvcResult.getResponse().getStatus());

        //授权，但是参数不合法
        mvcResult = mvc.perform(
                post("/publish")
                        .content(JSON.toJSONString(param))
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        assertEquals(DATA_BINDING_FAILED,
                ((Result) JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(),
                        Result.class)).getCode());


        param.setBrandId(1L);
        param.setBrandName("小米");
        param.setBrandAlias("MI");
        param.setAvatar("11");
        param.setCategoryId(1L);
        param.setName("小米8");
        param.setPrice(BigDecimal.valueOf(2000));
        param.setStock(1000);

        //授权，参数合法
        mvcResult = mvc.perform(
                post("/publish")
                        .content(JSON.toJSONString(param))
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());

        Result res = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(),
                Result.class);
        assertEquals(SUCCESS, res.getCode());
    }
}