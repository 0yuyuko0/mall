package com.yuyuko.mall.cart.service;

import com.yuyuko.mall.cart.bo.ShopCartItemBO;
import com.yuyuko.mall.cart.param.CartAddParam;
import com.yuyuko.mall.product.api.ProductService;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTests {
    @Autowired
    CartService cartService;

    @Mock
    ProductService productService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(cartService, "productService", productService);
    }

    @Test
    public void addCartItem() {
        CartAddParam cartAddParam = new CartAddParam();
        cartAddParam.setShopId(1L);
        cartAddParam.setShopName("shit");
        cartAddParam.setProductId(59L);
        cartAddParam.setCount(3);
        cartService.addCartItem(1L, cartAddParam);
    }

    @Test
    public void updateCartItem() {
        CartAddParam cartAddParam = new CartAddParam();
        cartAddParam.setShopId(1L);
        cartAddParam.setShopName("shit");
        cartAddParam.setProductId(59L);
        cartAddParam.setCount(-2);
        cartService.addCartItem(1L, cartAddParam);
    }

    @Test
    public void getCart() {
        given(productService.listCartItemProducts(List.of(59L))).willReturn(
                List.of(new CartItemProductDTO().setId(1L)));
        List<ShopCartItemBO> cart = cartService.getCart(1L);
        cart.forEach(System.out::println);
    }

    @Test
    public void removeCartItem() {
        CartAddParam cartAddParam = new CartAddParam();
        cartAddParam.setShopId(1L);
        cartAddParam.setShopName("shit");
        cartAddParam.setProductId(59L);
        cartAddParam.setCount(-1);
        cartService.addCartItem(1L, cartAddParam);
    }
}