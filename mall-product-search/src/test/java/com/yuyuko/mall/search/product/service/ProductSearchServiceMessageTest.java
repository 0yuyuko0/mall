package com.yuyuko.mall.search.product.service;

import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.product.message.ProductCreateMessage;
import com.yuyuko.mall.search.product.config.MessageCodecConfig;
import com.yuyuko.mall.search.product.dao.ProductRepository;
import com.yuyuko.mall.search.product.entity.Product;
import com.yuyuko.mall.test.autoconfigure.rocketmq.RocketMQTest;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * rocketmq集成测试
 */
@RocketMQTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                ProductSearchService.class,
                ProductSearchService.ProductCreateListener.class
        })
)
@Import({
        MessageCodecConfig.class
})
class ProductSearchServiceMessageTest {
    @Autowired
    private ProductSearchService searchService;

    @MockBean
    private ProductRepository productRepository;

    @SpyBean
    private MessageCodec messageCodec;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void onProductCreateMessage() throws InterruptedException {
        AtomicBoolean done = new AtomicBoolean();
        when(productRepository.save(any())).then(invocation -> {
            Product product = invocation.getArgument(0);
            assertEquals(1L, product.getId());
            done.set(true);
            return null;
        });

        ProductCreateMessage createMessage = new ProductCreateMessage();
        createMessage.setId(1L);
        rocketMQTemplate.send("product:create",
                MessageBuilder.withPayload(messageCodec.encode(createMessage)).build()
        );

        while (!done.get())
            TimeUnit.MILLISECONDS.sleep(500);
    }
}
