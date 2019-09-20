package com.yuyuko.mall.search.product.service;

import com.yuyuko.mall.search.product.entity.Product;
import com.yuyuko.mall.search.product.param.ProductSearchParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSearchServiceTests {
    @Autowired
    ProductSearchService productSearchService;


    @Test
    public void searchProducts() {
        ProductSearchParam productSearchParam = new ProductSearchParam();
        productSearchParam.setKeyword("小米");
        Page<Product> products = productSearchService.searchProducts(productSearchParam);
        System.out.println(products);
    }
}