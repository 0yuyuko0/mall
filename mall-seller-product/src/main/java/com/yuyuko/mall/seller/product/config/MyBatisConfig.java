package com.yuyuko.mall.seller.product.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yuyuko.mall.seller.product.dao")
public class MyBatisConfig {

}