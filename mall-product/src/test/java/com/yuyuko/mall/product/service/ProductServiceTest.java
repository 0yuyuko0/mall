package com.yuyuko.mall.product.service;

import com.yuyuko.mall.product.bo.ProductBO;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.product.manager.ProductManager;
import com.yuyuko.mall.shop.api.ShopRemotingService;
import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.stock.api.StockRemotingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductManager productManager;

    @Mock
    private ShopRemotingService shopService;

    @Mock
    private StockRemotingService stockRemotingService;

    @Test
    void getProduct() {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setShopId(1L);
        when(productManager.getProduct(1L)).thenReturn(product);
        when(stockRemotingService.getStock(1L)).thenReturn(1000);
        when(shopService.getShopInfo(1L)).thenReturn(new ShopDTO());

        ProductBO productBO = productService.getProduct(1L);

        assertEquals(1000, productBO.getStock());
        assertNotNull(productBO.getShopInfo());
    }

}