package com.yuyuko.mall.cart.service;

import com.yuyuko.mall.cart.bo.ShopCartItemBO;
import com.yuyuko.mall.cart.config.IdGeneratorConfig;
import com.yuyuko.mall.cart.dao.CartDao;
import com.yuyuko.mall.cart.dto.CartItemDTO;
import com.yuyuko.mall.cart.entity.CartItemDO;
import com.yuyuko.mall.cart.param.CartAddParam;
import com.yuyuko.mall.cart.param.CartUpdateParam;
import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.test.utils.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@MybatisTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                CartService.class
        }
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
        IdGeneratorConfig.class
)
class CartServiceTest {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDao cartDao;

    @BeforeEach
    void insertTestData() throws InterruptedException {
        cartDao.insertCartItem(new CartItemDO()
                .setId(-1L)
                .setUserId(-1L)
                .setProductId(-1L)
                .setCount(3)
                .setShopId(-1L)
                .setShopName("红魔馆")
        );
        cartDao.insertCartItem(new CartItemDO()
                .setId(-4L)
                .setUserId(-1L)
                .setProductId(-4L)
                .setCount(1)
                .setShopId(-2L)
                .setShopName("白玉楼")
        );
        cartDao.insertCartItem(new CartItemDO()
                .setId(-3L)
                .setUserId(-1L)
                .setProductId(-3L)
                .setCount(2)
                .setShopId(-2L)
                .setShopName("白玉楼")
        );
        cartDao.insertCartItem(new CartItemDO()
                .setId(-2L)
                .setUserId(-1L)
                .setProductId(-2L)
                .setCount(2)
                .setShopId(-1L)
                .setShopName("红魔馆")
        );
    }

    @MockBean
    private ProductRemotingService productRemotingService;

    @Test
    void getCart() {
        org.springframework.test.util.ReflectionTestUtils.setField(cartService,
                "productRemotingService", productRemotingService);
        when(productRemotingService.listCartItemProducts(anyList()))
                .thenAnswer(invocation -> ((List<Long>) invocation.getArgument(0)).stream().map(
                        id -> new CartItemProductDTO().setId(id)
                ).collect(Collectors.toList()));
        List<ShopCartItemBO> cart = cartService.getCart(-1L);
        assertEquals(2, cart.size());
        assertEquals(2, cart.get(0).getCartItems().size());
        assertEquals(2, cart.get(1).getCartItems().size());
        assertTrue(ReflectionTestUtils.isCollectionElementsAllFieldsNotNull(cart));
    }

    @Test
    void deleteCartItem() {
        cartService.deleteCartItem(-1L);

        List<CartItemDTO> cart = cartDao.getCart(-1L);
        assertEquals(3, cart.size());
    }

    @Test
    void addCartItem() {
        cartService.addCartItem(-1L, new CartAddParam()
                .setProductId(-5L)
                .setCount(5)
                .setShopId(-3L)
                .setShopName("妖怪之山")
        );
        assertNotNull(cartDao.getCartItem(-1L, -5L));
        cartService.addCartItem(-1L, new CartAddParam()
                .setProductId(-1L)
                .setCount(5)
        );
        assertEquals(8, cartDao.getCartItem(-1L, -1L).getCount());
    }

    @Test
    void reduceCartItem() {
        cartService.reduceCartItem(-1L, -1L);
        assertEquals(2, cartDao.getCartItem(-1L, -1L).getCount());

    }

    @Test
    void updateCartItem() {
        cartService.updateCartItem(-1L, new CartUpdateParam()
                .setId(-1L)
                .setCount(1)
        );
        assertEquals(1, cartDao.getCartItem(-1L, -1L).getCount());

    }
}