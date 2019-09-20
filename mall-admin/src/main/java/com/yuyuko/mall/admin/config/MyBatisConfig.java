package com.yuyuko.mall.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yuyuko.mall.admin.dao")
public class MyBatisConfig {
}
