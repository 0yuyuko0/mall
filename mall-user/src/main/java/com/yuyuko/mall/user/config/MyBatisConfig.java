package com.yuyuko.mall.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yuyuko.mall.user.dao")
public class MyBatisConfig {
}
