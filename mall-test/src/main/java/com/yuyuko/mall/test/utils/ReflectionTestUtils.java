package com.yuyuko.mall.test.utils;

import org.joor.Reflect;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class ReflectionTestUtils {
    public static <T> boolean isAllFieldsNotNull(T o) {
        if (o == null) return true;
        return Reflect.on(o).fields().values().stream()
                .allMatch(f -> f.get() != null);
    }

    public static <T> boolean isCollectionElementsAllFieldsNotNull(Collection<T> list) {
        if (list == null || list.size() == 0)
            return true;
        return list.stream().map(Reflect::on).allMatch(
                o -> o.fields().values().stream().allMatch(f -> f.get() != null)
        );
    }
}
