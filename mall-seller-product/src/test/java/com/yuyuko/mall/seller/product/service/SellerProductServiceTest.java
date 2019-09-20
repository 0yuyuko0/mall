package com.yuyuko.mall.seller.product.service;

import com.yuyuko.mall.seller.product.param.ProductPublishParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerProductServiceTest {
    @Autowired
    private SellerProductService sellerProductService;


    @Test
    public void testPublishProduct() throws Exception {
        ProductPublishParam publishParam = new ProductPublishParam
                (1L, "小米", "MI", 1L, "小米9透明尊享版",
                        BigDecimal.valueOf(200000, 2), 1000, "shit");
        sellerProductService.publishProduct(1L, publishParam);
    }
}