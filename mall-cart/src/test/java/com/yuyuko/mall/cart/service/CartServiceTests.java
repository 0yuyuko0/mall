package com.yuyuko.mall.cart.service;

import com.yuyuko.mall.cart.bo.ShopCartItemBO;
import com.yuyuko.mall.cart.param.CartUpdateParam;
import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTests {
    @Autowired
    CartService cartService;

    @Mock
    ProductRemotingService productRemotingService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(cartService, "productRemotingService", productRemotingService);
    }

    @Test
    public void addCartItem() {
        CartUpdateParam cartUpdateParam = new CartUpdateParam();
        cartUpdateParam.setShopId(1L);
        cartUpdateParam.setShopName("shit");
        cartUpdateParam.setProductId(59L);
        cartUpdateParam.setCount(3);
        cartService.updateCartItem(1L, cartUpdateParam);
    }

    @Test
    public void updateCartItem() {
        CartUpdateParam cartUpdateParam = new CartUpdateParam();
        cartUpdateParam.setShopId(1L);
        cartUpdateParam.setShopName("shit");
        cartUpdateParam.setProductId(59L);
        cartUpdateParam.setCount(-2);
        cartService.updateCartItem(1L, cartUpdateParam);
    }

    @Test
    public void getCart() {
        given(productRemotingService.listCartItemProducts(List.of(59L))).willReturn(
                List.of(new CartItemProductDTO().setId(1L)));
        List<ShopCartItemBO> cart = cartService.getCart(1L);
        cart.forEach(System.out::println);
    }

    @Test
    public void removeCartItem() {
        CartUpdateParam cartUpdateParam = new CartUpdateParam();
        cartUpdateParam.setShopId(1L);
        cartUpdateParam.setShopName("shit");
        cartUpdateParam.setProductId(59L);
        cartUpdateParam.setCount(-1);
        cartService.updateCartItem(1L, cartUpdateParam);
    }
}