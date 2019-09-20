package com.yuyuko.mall.product.config;

import com.yuyuko.mall.redis.core.RedisExpires;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisExpiresConfig {

    @Bean(initMethod = "init")
    @ConfigurationProperties("redis.expires.product")
    public RedisExpires productRedisExpires() {
        return new RedisExpires();
    }
}
