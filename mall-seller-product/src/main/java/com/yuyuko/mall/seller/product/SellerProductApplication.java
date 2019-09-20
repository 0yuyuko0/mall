package com.yuyuko.mall.seller.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SellerProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellerProductApplication.class, args);
    }

}
