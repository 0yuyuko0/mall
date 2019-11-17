package com.yuyuko.mall.search.product.service;

import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.product.message.ProductCreateMessage;
import com.yuyuko.mall.search.product.config.ElasticsearchConfig;
import com.yuyuko.mall.search.product.config.MessageCodecConfig;
import com.yuyuko.mall.search.product.dao.ProductRepository;
import com.yuyuko.mall.search.product.entity.Product;
import com.yuyuko.mall.search.product.param.ProductSearchParam;
import com.yuyuko.mall.search.product.param.ProductSearchSort;
import com.yuyuko.mall.test.autoconfigure.elasticsearch.ElasticsearchTest;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ElasticsearchTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                ProductSearchService.class,
                ProductSearchService.ProductCreateListener.class
        })
)
@Import({
        ElasticsearchConfig.class,
        MessageCodecConfig.class
})
class ProductSearchServiceTest {
    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private ProductSearchService.ProductCreateListener productCreateListener;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MessageCodec messageCodec;

    @BeforeEach
    void createProduct() {
        ProductCreateMessage createMessage = new ProductCreateMessage();
        createMessage.setId(1000L);
        createMessage.setName("iphone 11");
        createMessage.setStock(100);
        createMessage.setAvatar("123");
        createMessage.setBrandId(1L);
        createMessage.setBrandAlias("APPLE");
        createMessage.setBrandName("苹果");
        createMessage.setCategoryId(1L);
        createMessage.setPrice(BigDecimal.valueOf(8000));
        createMessage.setShopId(1L);
        createMessage.setShopName("苹果自营专卖店");
        createMessage.setTimeCreate(LocalDateTime.now());

        MessageExt messageExt = new MessageExt();
        messageExt.setBody(messageCodec.encode(createMessage));

        productCreateListener.onMessage(messageExt);

        assertTrue(productRepository.existsById(1000L));
    }

    @AfterEach
    void deleteProduct() {
        productRepository.deleteById(1000L);
    }

    @Test
    void searchProducts() {
        ProductSearchParam productSearchParam = new ProductSearchParam();
        productSearchParam.setKeyword("iphone");
        assertTrue(productSearchService.searchProducts(productSearchParam).getContent().size() > 0);
        productSearchParam.setKeyword("iphone")
                .setBrandId(1L)
                .setPage(0)
                .setPrice(new ProductSearchParam.PriceRange(1000, 10000))
                .setSort(ProductSearchSort.PRICE_LOWER);
        assertTrue(productSearchService.searchProducts(productSearchParam).getContent().size() > 0);
    }
}