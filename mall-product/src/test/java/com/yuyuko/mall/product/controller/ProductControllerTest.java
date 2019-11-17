package com.yuyuko.mall.product.controller;

import com.yuyuko.mall.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getProduct() throws Exception {
        // no param
        MvcResult result = mockMvc.perform(
                get("/get")
        ).andReturn();
        assertNotEquals(200, result.getResponse().getStatus());
        result = mockMvc.perform(
                get("/get")
                        .param("productId", "1")
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());

    }
}