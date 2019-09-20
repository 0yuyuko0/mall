package com.yuyuko.mall.cart.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yuyuko.mall.cart.dao")
public class MyBatisConfig {

}