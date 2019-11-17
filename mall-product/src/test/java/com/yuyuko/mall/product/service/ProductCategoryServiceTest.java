package com.yuyuko.mall.product.service;

import com.yuyuko.mall.product.dao.BrandDao;
import com.yuyuko.mall.product.dao.ProductCategoryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {
    @InjectMocks
    private ProductCategoryService service;

    @Mock
    private ProductCategoryDao productCategoryDao;

    @Mock
    private BrandDao brandDao;

    @Test
    void listChildCategories() {
        when(productCategoryDao.listChildCategories(anyLong())).thenReturn(List.of());

        assertNotNull(service.listChildCategories(1L));
    }

    @Test
    void listBrandsOfCategory() {
        when(brandDao.listBrandsOfCategory(anyLong())).thenReturn(List.of());

        assertNotNull(service.listBrandsOfCategory(1L));
    }
}