package com.yuyuko.mall.search.product.controller;

import com.yuyuko.mall.search.product.service.ProductSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductSearchController.class)
class ProductSearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductSearchService productSearchService;

    @Test
    void searchProducts() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/")
        ).andReturn();
        assertNotEquals(200, result.getResponse().getStatus());


        result = mockMvc.perform(
                get("/")
                        .param("keyword", "小米")
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}