package com.yuyuko.mall.shop.config;

import com.yuyuko.mall.redis.core.RedisExpires;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisExpiresConfig {
    @Bean(initMethod = "init")
    @ConfigurationProperties("redis.expires.shop")
    public RedisExpires redisExpires() {
        return new RedisExpires();
    }
}
