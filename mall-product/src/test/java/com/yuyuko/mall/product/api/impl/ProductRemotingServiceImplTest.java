package com.yuyuko.mall.product.api.impl;

import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.dao.ProductDao;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.product.entity.ProductDO;
import com.yuyuko.mall.product.manager.ProductManager;
import com.yuyuko.mall.product.param.ProductCreateParam;
import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.param.StockCreateParam;
import com.yuyuko.mall.test.autoconfigure.dubbo.DubboTest;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DubboTest
class ProductRemotingServiceImplTest {
    @Reference
    private ProductRemotingService productRemotingService;

    @MockBean
    private ProductDao productDao;

    @MockBean
    private ProductManager productManager;

    @MockBean
    private StockRemotingService stockRemotingService;

    @Autowired
    private ProductRemotingService productRemotingServiceBean;

    void mockStockRemotingService() {
        ReflectionTestUtils.setField(productRemotingServiceBean, "stockRemotingService",
                stockRemotingService);
    }

    @Test
    void createProduct() {
        mockStockRemotingService();
        ProductCreateParam createParam = new ProductCreateParam();
        createParam.setId(1L);
        createParam.setStock(1000);

        when(productDao.insert(any())).thenAnswer(invocation -> {
            ProductDO productDO = invocation.getArgument(0);
            assertEquals(1L, productDO.getId());
            return null;
        });

        productRemotingService.createProduct(createParam);

        verify(stockRemotingService).createProductStock(new StockCreateParam(1L, 1000));
    }

    @ParameterizedTest
    @MethodSource("listCartItemProductsGen")
    void listCartItemProducts(List<Long> productIds) {
        mockStockRemotingService();
        when(productDao.listCartItemProducts(anyList())).thenAnswer(invocation -> {
            List<Long> ids = invocation.getArgument(0);
            return ids.stream().map(id -> new CartItemProductDTO().setId(id)).collect(Collectors.toList());
        });
        when(stockRemotingService.listStocks(anyList())).thenAnswer(invocation -> {
            List<Long> ids = invocation.getArgument(0);
            return ids.stream().map(Long::intValue).collect(Collectors.toList());
        });

        List<CartItemProductDTO> cartItemProducts =
                productRemotingService.listCartItemProducts(productIds);
        assertTrue(cartItemProducts.stream().allMatch(cartItemProduct ->
                cartItemProduct.getId().intValue() == cartItemProduct.getStock()));
    }

    static Stream<List<Long>> listCartItemProductsGen() {
        return Stream.of(
                List.of(1L),
                List.of(1L, 2L),
                List.of(1L, 2L, 3L)
        );
    }

    @Test
    void exist() {
        when(productManager.getProduct(anyLong())).thenReturn(null);

        assertFalse(productRemotingService.exist(1L));

        when(productManager.getProduct(anyLong())).thenReturn(new ProductDTO());

        assertTrue(productRemotingService.exist(1L));
    }
}