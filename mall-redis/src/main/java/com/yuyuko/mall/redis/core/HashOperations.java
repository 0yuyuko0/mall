package com.yuyuko.mall.redis.core;

import org.joor.Reflect;
import org.springframework.util.StringUtils;

import java.util.*;

public class HashOperations {
    private org.springframework.data.redis.core.HashOperations<String, String, byte[]> hashOperations;

    private RedisUtils redisUtils;

    public HashOperations(RedisUtils redisUtils) {
        this.hashOperations = redisUtils.getRedisTemplate().opsForHash();
        this.redisUtils = redisUtils;
    }

    public void putObject(String key, Object o) {
        if (StringUtils.isEmpty(key) || o == null)
            throw new NullPointerException();
        Map<String, byte[]> m = redisUtils.getRedisCodec().encodeFields(o);
        hashOperations.putAll(key, m);
    }

    public void putObject(String key, Object o, RedisExpires redisExpires) {
        if (StringUtils.isEmpty(key) || o == null)
            throw new NullPointerException();
        Map<String, byte[]> m = redisUtils.getRedisCodec().encodeFields(o);

        redisUtils.multi();
        hashOperations.putAll(key, m);
        redisUtils.expire(key, redisExpires);
        redisUtils.exec();
    }

    public void put(String key, String hashKey, Object o) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey) || o == null)
            throw new NullPointerException();
        hashOperations.put(key, hashKey, redisUtils.getRedisCodec().encode(o));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        Map<String, byte[]> m = hashOperations.entries(key);
        return redisUtils.getRedisCodec().decodeFields(m, clazz);
    }

    public <T> T get(String key, String hashKey, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey) || clazz == null)
            throw new NullPointerException();
        return redisUtils.getRedisCodec().decode(hashOperations.get(key, hashKey), clazz);
    }

    public <T> T getSimilarShapeObject(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        Set<String> fieldNameSet = Reflect.onClass(clazz).fields().keySet();
        List<byte[]> fieldValBytesList = hashOperations.multiGet(key, fieldNameSet);
        return redisUtils.getRedisCodec().decodeFields(convertToMap(fieldNameSet, fieldValBytesList), clazz);
    }

    private static <K, V> Map<K, V> convertToMap(Collection<K> keys, Collection<V> vals) {
        Iterator<K> keyIter = keys.iterator();
        Iterator<V> valIter = vals.iterator();
        Map<K, V> map = new HashMap<>();
        while (keyIter.hasNext() && valIter.hasNext())
            map.put(keyIter.next(), valIter.next());
        return map;
    }
}
