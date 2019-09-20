package com.yuyuko.mall.redis.autoconfigure;

import com.yuyuko.mall.redis.core.RedisUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisUtilsAutoConfiguration {
    @Bean
    public RedisTemplate<String, byte[]> redisUtilsTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setEnableDefaultSerializer(false);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(byteRedisSerializer());
        redisTemplate.setHashValueSerializer(byteRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<byte[]> byteRedisSerializer() {
        return new RedisSerializer<byte[]>() {
            @Override
            public byte[] serialize(byte[] bytes) throws SerializationException {
                return bytes;
            }

            @Override
            public byte[] deserialize(byte[] bytes) throws SerializationException {
                return bytes;
            }
        };
    }

    @Bean
    public RedisUtils redisUtils(RedisTemplate<String, byte[]> redisTemplate) {
        return new RedisUtils(redisTemplate);
    }
}
