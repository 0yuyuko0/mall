package com.yuyuko.mall.redis.core;

import com.yuyuko.mall.redis.protostuff.ProtoStuffUtils;
import org.joor.Reflect;
import org.springframework.data.redis.connection.ReactiveClusterZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
        Map<String, byte[]> m = ProtoStuffUtils.serializeFields(o);
        hashOperations.putAll(key, m);
    }

    public void putObject(String key, Object o, RedisExpires redisExpires) {
        if (StringUtils.isEmpty(key) || o == null)
            throw new NullPointerException();
        Map<String, byte[]> m = ProtoStuffUtils.serializeFields(o);

        redisUtils.multi();
        hashOperations.putAll(key, m);
        redisUtils.expire(key, redisExpires);
        redisUtils.exec();
    }

    public void put(String key, String hashKey, Object o) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey) || o == null)
            throw new NullPointerException();
        hashOperations.put(key, hashKey, ProtoStuffUtils.serialize(o));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        Map<String, byte[]> m = hashOperations.entries(key);
        return ProtoStuffUtils.deserializeFromFields(m, clazz);
    }

    public <T> T get(String key, String hashKey, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(hashKey) || clazz == null)
            throw new NullPointerException();
        return ProtoStuffUtils.deserialize(hashOperations.get(key, hashKey), clazz);
    }

    public <T> T getSimilarShapeObject(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key) || clazz == null)
            throw new NullPointerException();
        Set<String> fieldNameSet = Reflect.onClass(clazz).fields().keySet();
        List<byte[]> fieldValBytesList = hashOperations.multiGet(key, fieldNameSet);
        return ProtoStuffUtils.deserializeFromFields(convertToMap(fieldNameSet, fieldValBytesList), clazz);
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
