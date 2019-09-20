package com.yuyuko.mall.shop.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yuyuko.mall.shop.dao")
public class MyBatisConfig {

}