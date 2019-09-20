package com.yuyuko.mall.redis.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.util.ReflectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;


public class RedisUtils implements InitializingBean {
    private RedisTemplate<String, byte[]> redisTemplate;

    private ValueOperations valueOperations;

    private HashOperations hashOperations;

    public ValueOperations opsForValue() {
        return valueOperations;
    }

    public HashOperations opsForHash() {
        return hashOperations;
    }

    public void expire(String key, RedisExpires redisExpires) {
        redisTemplate.expire(key, redisExpires.getExpires(), TimeUnit.MILLISECONDS);
    }

    public void multi() {
        redisTemplate.multi();
    }

    public List<Object> exec() {
        return redisTemplate.exec();
    }

    public List<Object> executePipelined(RedisCallback redisCallback) {
        return redisTemplate.executePipelined(redisCallback);
    }

    public RedisTemplate<String, byte[]> getRedisTemplate() {
        return redisTemplate;
    }

    public RedisUtils(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        this.valueOperations = new ValueOperations(this);
        this.hashOperations = new HashOperations(this);
    }
}