package com.yuyuko.mall.seller.product.service;

import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.param.ProductCreateParam;
import com.yuyuko.mall.seller.api.SellerRemotingService;
import com.yuyuko.mall.seller.dto.SellerShopInfoDTO;
import com.yuyuko.mall.seller.product.config.IdGeneratorConfig;
import com.yuyuko.mall.seller.product.config.MessageCodecConfig;
import com.yuyuko.mall.seller.product.param.ProductPublishParam;
import com.yuyuko.mall.test.autoconfigure.rocketmq.RocketMQTest;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@RocketMQTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                SellerProductService.class,
                SellerProductService.ProductCreateListener.class
        }
))
@Import({
        IdGeneratorConfig.class,
        MessageCodecConfig.class
})
class SellerProductServiceMessageTest {
    @Autowired
    private SellerProductService sellerProductService;

    @MockBean
    private ProductRemotingService productRemotingService;

    @MockBean
    private SellerRemotingService sellerRemotingService;

    @SpyBean
    private SellerProductService.ProductCreateListener productCreateListener;

    @BeforeEach
    void mock() {
        ReflectionTestUtils.setField(sellerProductService, "productRemotingService",
                productRemotingService);
        ReflectionTestUtils.setField(sellerProductService, "sellerRemotingService",
                sellerRemotingService);
    }

    @Test
    void publishProduct() throws Exception {
        AtomicBoolean done = new AtomicBoolean();

        ProductPublishParam publishParam = new ProductPublishParam
                (1L, "小米", "MI", 1L, "小米9透明尊享版",
                        BigDecimal.valueOf(200000, 2), 1000, "shit");
        when(sellerRemotingService.getSellerShopInfo(anyLong()))
                .thenReturn(new SellerShopInfoDTO(1L, "店铺"));
        doAnswer(invocation -> {
            ProductCreateParam createParam = invocation.getArgument(0);
            Field[] fields = createParam.getClass().getDeclaredFields();
            assertTrue(Arrays.stream(fields).allMatch(field ->
                    ReflectionTestUtils.getField(createParam, field.getName()) != null));
            done.set(true);
            return null;
        }).when(productRemotingService).createProduct(any());

        sellerProductService.publishProduct(1L, publishParam);

        while (!done.get())
            TimeUnit.MILLISECONDS.sleep(500);

        done.set(false);
        reset(productRemotingService);
        reset(productCreateListener);


        doThrow(RuntimeException.class).when(productRemotingService).createProduct(any());
        when(productRemotingService.exist(anyLong())).thenReturn(true);
        when(productCreateListener.checkLocalTransaction(any())).thenAnswer(invocation -> {
            Object state = invocation.callRealMethod();
            assertEquals(RocketMQLocalTransactionState.COMMIT, state);
            done.set(true);
            return state;
        });

        sellerProductService.publishProduct(1L, publishParam);

        while (!done.get())
            TimeUnit.MILLISECONDS.sleep(500);
    }
}