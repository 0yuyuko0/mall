package com.yuyuko.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.order.aspect.BindingResultAspect;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.order.param.OrderCreateParam;
import com.yuyuko.mall.order.param.OrderItemCreateParam;
import com.yuyuko.mall.order.param.ShopOrderCreateParam;
import com.yuyuko.mall.order.result.OrderResult;
import com.yuyuko.mall.order.service.OrderService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(value = OrderController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = BindingResultAspect.class
        )
)
@ImportAutoConfiguration(AopAutoConfiguration.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void getOrder() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));
        //少订单号
        MvcResult result = mockMvc.perform(
                get("/get")
                        .session(session)
        ).andReturn();
        assertNotEquals(200, result.getResponse().getStatus());
        result = mockMvc.perform(
                get("/get")
                        .session(session)
                        .param("orderId", "1")
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        verify(orderService, times(1)).getOrder(1L, 1L);
    }

    @Test
    void createOrder() throws Exception {
        OrderCreateParam createParam = new OrderCreateParam()
                .setActualPayment(BigDecimal.ZERO)
                .setConsigneeName("")
                .setConsigneePhoneNumber("12345678912")
                .setDeliveryAddress("")
                .setDeliveryFee(BigDecimal.ZERO)
                .setPaymentMethod("")
                .setTotalPrice(BigDecimal.ZERO)
                .setShopOrders(
                        List.of(new ShopOrderCreateParam()
                                .setShopId(1L)
                                .setShopName("")
                                .setDeliveryFee(BigDecimal.ZERO)
                                .setTotalPrice(BigDecimal.ZERO)
                                .setActualPayment(BigDecimal.ZERO)
                                .setDeliveryMethod("")
                                .setTimeExpectedDelivery("")
                                .setOrderItems(
                                        List.of(
                                                new OrderItemCreateParam()
                                                        .setId(1L)
                                                        .setName("")
                                                        .setPrice(BigDecimal.ZERO)
                                                        .setAvatar("")
                                                        .setCount(1)
                                        ))
                        ));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L).setNickname("yuyuko"));
        //novalid content
        MvcResult result = mockMvc.perform(
                post("/create")
                        .content(new byte[0])
                        .session(session)
        ).andReturn();

        assertNotEquals(200, result.getResponse().getStatus());

        result = mockMvc.perform(
                post("/create")
                        .content(JSON.toJSONString(createParam))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .session(session)
        ).andReturn();

        assertEquals(SUCCESS,
                ((Result) JSON.parseObject(result.getResponse().getContentAsByteArray(),
                        Result.class)).getCode());

        verify(orderService, times(1)).createOrder(eq(1L), anyString(), any());
    }

    @Test
    void payOrder() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));

        MvcResult result = mockMvc.perform(
                post("/pay")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("1")
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());

        verify(orderService, times(1)).payOrder(eq(1L), eq(1L));

        doThrow(StockNotEnoughException.class).when(orderService).payOrder(anyLong(), anyLong());

        result = mockMvc.perform(
                post("/pay")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("1")
        ).andReturn();


        assertEquals(OrderResult.STOCK_NOT_ENOUGH,
                ((Result) JSON.parseObject(result.getResponse().getContentAsByteArray(),
                        Result.class)).getCode());

    }

    @Test
    void cancelOrder() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userSessionInfo", new UserSessionInfo(1L));

        MvcResult result = mockMvc.perform(
                post("/cancel")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("1")
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        verify(orderService, times(1)).cancelOrder(eq(1L), eq(1L));
    }
}