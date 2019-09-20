package com.yuyuko.mall.redis.core;

import com.yuyuko.mall.redis.protostuff.ProtoStuffUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ValueOperations {
    private org.springframework.data.redis.core.ValueOperations<String, byte[]> valueOperations;
    private RedisUtils redisUtils;

    ValueOperations(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
        this.valueOperations = redisUtils.getRedisTemplate().opsForValue();
    }

    public <T> T get(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        byte[] bytes = valueOperations.get(key);
        return ProtoStuffUtils.deserialize(bytes, clazz);
    }

    public <T> List<T> multiGet(List<String> key, Class<T> clazz) {
        if (CollectionUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        List<byte[]> bytesList = valueOperations.multiGet(key);
        return bytesList.stream().map(bytes -> ProtoStuffUtils.deserialize(bytes, clazz))
                .collect(Collectors.toList());
    }

    public void set(String key, Object value) {
        if (StringUtils.isEmpty(key) || value == null)
            throw new NullPointerException();
        byte[] bytes = ProtoStuffUtils.serialize(value);
        valueOperations.set(key, bytes);
    }

    public void set(String key, Object value, RedisExpires redisExpires) {
        if (StringUtils.isEmpty(key) || value == null || redisExpires == null)
            throw new NullPointerException();
        byte[] bytes = ProtoStuffUtils.serialize(value);
        valueOperations.set(key, bytes, redisExpires.getExpires(), TimeUnit.MILLISECONDS);
    }

    /**
     * @param key          redis键
     * @param value        redis值
     * @param redisExpires 过期对象
     * @return 如果缓存存在则不set，返回存在的值。如果不存在，则set并返回set的值
     */
    @SuppressWarnings("unchecked")
    public <T> T setAndGet(String key, T value, RedisExpires redisExpires) {
        if (StringUtils.isEmpty(key) || value == null || redisExpires == null)
            throw new NullPointerException();
        byte[] bytes = ProtoStuffUtils.serialize(value);
        List<Object> exec = redisUtils.executePipelined(
                connection -> {
                    byte[] keyBytes = StringRedisSerializer.UTF_8.serialize(key);
                    connection.getSet(keyBytes, bytes);
                    connection.pExpire(keyBytes, redisExpires.getExpires());
                    return null;
                }
        );
        byte[] old = (byte[]) exec.get(0);
        if (old != null)
            return (T) ProtoStuffUtils.deserialize(old, value.getClass());
        return value;
    }

    public <T> void multiSetIfAbsent(List<String> keys,
                                List<T> values,
                                Class<T> valClass,
                                RedisExpires redisExpires) {
        if (CollectionUtils.isEmpty(keys) || CollectionUtils.isEmpty(values) || redisExpires == null)
            throw new NullPointerException();
        if (keys.size() != values.size())
            throw new IllegalArgumentException();
        Iterator<String> keyIter = keys.iterator();
        Iterator<T> valIter = values.iterator();
        Map<String, byte[]> m = new HashMap<>();
        while (keyIter.hasNext() && valIter.hasNext())
            m.put(keyIter.next(), ProtoStuffUtils.serialize(valIter.next()));
        valueOperations.multiSetIfAbsent(m);
    }

    /**
     * setAndGet的multi版
     */
    public <T> List<T> multiSetAndGet(List<String> keys,
                                      List<T> values,
                                      Class<T> valClass,
                                      RedisExpires redisExpires) {
        if (CollectionUtils.isEmpty(keys) || CollectionUtils.isEmpty(values) || redisExpires == null)
            throw new NullPointerException();
        if (keys.size() != values.size())
            throw new IllegalArgumentException();
        List<Object> exec = redisUtils.executePipelined(
                connection -> {
                    Iterator<String> keyIter = keys.iterator();
                    Iterator<T> valIter = values.iterator();
                    while (keyIter.hasNext() && valIter.hasNext()) {
                        String key = keyIter.next();
                        T val = valIter.next();
                        byte[] keyBytes = StringRedisSerializer.UTF_8.serialize(key);
                        connection.getSet(keyBytes, ProtoStuffUtils.serialize(val));
                        connection.pExpire(keyBytes, redisExpires.getExpires());
                    }
                    return null;
                }
        );
        ListIterator<Object> execIter = exec.listIterator();
        List<T> res = new ArrayList<>();
        while (execIter.hasNext())
            if (execIter.nextIndex() % 2 == 0)
                res.add(ProtoStuffUtils.deserialize((byte[]) execIter.next(), valClass));
        return res;
    }
}
