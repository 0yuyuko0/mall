package com.yuyuko.mall.product.controller;

import com.yuyuko.mall.product.service.ProductCategoryService;
import com.yuyuko.mall.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductCategoryController.class)
class ProductCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductCategoryService productCategoryService;

    @Test
    void listChildCategories() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/category/list")
        ).andReturn();
        assertNotEquals(200, result.getResponse().getStatus());

        result = mockMvc.perform(
                get("/category/list")
                        .param("parentId", "-1")
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void listBrandsOfCategory() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/category/brand/list")
        ).andReturn();
        assertNotEquals(200, result.getResponse().getStatus());

        result = mockMvc.perform(
                get("/category/brand/list")
                        .param("categoryId", "-1")
        ).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}