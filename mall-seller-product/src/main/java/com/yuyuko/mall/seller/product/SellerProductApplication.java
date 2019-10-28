package com.yuyuko.mall.seller.product;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableSwagger2Doc
public class SellerProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellerProductApplication.class, args);
    }

}
