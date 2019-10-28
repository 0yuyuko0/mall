package com.yuyuko.mall.common.message;

public interface MessageCodec {
    <T> byte[] encode(T obj);

    <T> T decode(byte[] data, Class<T> clazz);
}
