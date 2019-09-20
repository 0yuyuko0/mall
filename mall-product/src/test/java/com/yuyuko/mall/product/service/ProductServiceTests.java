package com.yuyuko.mall.product.service;

import com.yuyuko.mall.product.param.ProductCreateParam;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Test
    public void getProduct() {
        System.out.println(productService.getProduct(56L));
    }

    @Test
    public void createProduct() {
        ProductCreateParam createParam = new ProductCreateParam
                (1L, 1L, 1L, 1L, 1L, "小米9透明尊享版",
                        BigDecimal.valueOf(200000, 2), 1000, "shit");
        productService.createProduct(createParam);
    }
}