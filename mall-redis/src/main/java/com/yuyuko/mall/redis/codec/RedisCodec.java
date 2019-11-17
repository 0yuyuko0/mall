package com.yuyuko.mall.redis.codec;

import java.util.Map;

public interface RedisCodec {
    <T> byte[] encode(T o);

    <T> Map<String, byte[]> encodeFields(T o);

    <T> T decode(byte[] bytes, Class<T> clazz);

    <T> T decodeFields(Map<String, byte[]> m, Class<T> clazz);
}
