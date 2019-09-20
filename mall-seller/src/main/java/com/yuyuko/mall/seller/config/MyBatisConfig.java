package com.yuyuko.mall.seller.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yuyuko.mall.seller.dao")
public class MyBatisConfig {

}