package com.yuyuko.mall.common.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.joor.Reflect;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProtoStuffUtils {

    private static final ThreadLocal<LinkedBuffer> buffer = new ThreadLocal<>();

    private static LinkedBuffer getLinkedBuffer() {
        LinkedBuffer linkedBuffer = buffer.get();
        if (linkedBuffer != null)
            return linkedBuffer;
        LinkedBuffer newBuffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        buffer.set(newBuffer);
        return newBuffer;
    }

    public static <T> byte[] serialize(T o) {
        Objects.requireNonNull(o);
        byte[] bytes = null;
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(o.getClass());
        LinkedBuffer buffer = getLinkedBuffer();
        bytes = ProtostuffIOUtil.toByteArray(o, schema, buffer);
        buffer.clear();
        return bytes;
    }

    public static <T> Map<String, byte[]> serializeFields(T o) {
        Objects.requireNonNull(o);
        Map<String, Reflect> fields = Reflect.on(o).fields();
        Map<String, byte[]> res = new HashMap<>(fields.size());
        for (Map.Entry<String, Reflect> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue().get();
            if (fieldValue != null)
                res.put(fieldName, serialize(fieldValue));
        }
        return res;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T o = null;
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        o = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, o, schema);
        return o;
    }

    public static <T> T deserializeFromFields(Map<String, byte[]> bytesMap, Class<T> clazz) {
        Objects.requireNonNull(clazz);
        Reflect reflect = Reflect.onClass(clazz).create();
        for (Map.Entry<String, byte[]> entry : bytesMap.entrySet()) {
            String fieldName = entry.getKey();
            byte[] bytes = entry.getValue();
            if (bytes != null)
                reflect.set(fieldName, deserialize(bytes, reflect.field(fieldName).type()));
        }
        return reflect.get();
    }

}
