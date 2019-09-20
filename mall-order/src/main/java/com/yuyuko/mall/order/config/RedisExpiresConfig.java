package com.yuyuko.mall.order.config;

import com.yuyuko.mall.redis.core.RedisExpires;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisExpiresConfig {

    @ConfigurationProperties("redis.expires.order")
    @Bean(initMethod = "init")
    public RedisExpires orderRedisExpires() {
        return new RedisExpires();
    }
}
