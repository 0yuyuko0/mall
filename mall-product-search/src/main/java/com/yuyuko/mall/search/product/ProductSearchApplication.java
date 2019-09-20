package com.yuyuko.mall.search.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductSearchApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ProductSearchApplication.class, args);
    }

}
