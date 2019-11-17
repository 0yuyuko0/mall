package com.yuyuko.mall.common.utils;

import org.joor.Reflect;

import java.util.*;
import java.util.function.*;

public class CollectionUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static <F, T> List<T> transListToList(List<F> list, Function<F, T> func) {
        Objects.requireNonNull(func);
        if (isEmpty(list))
            return new ArrayList<>();
        List<T> newList = new ArrayList<>(list.size());
        list.forEach(item -> newList.add(func.apply(item)));
        return newList;
    }

    public static <F, S> void consume(List<F> list1, List<S> list2,
                                      BiConsumer<F, S> biConsumer) {
        Objects.requireNonNull(biConsumer);
        if (isEmpty(list1) && isEmpty(list2))
            return;
        if (list1.size() != list2.size())
            throw new IllegalArgumentException();
        Iterator<F> iterator1 = list1.iterator();
        Iterator<S> iterator2 = list2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            F first = iterator1.next();
            S second = iterator2.next();
            biConsumer.accept(first, second);
        }
    }

    public static <F, S> boolean test(List<F> list1, List<S> list2, BiPredicate<F, S> biPredicate) {
        if (isEmpty(list1) && isEmpty(list2))
            return true;
        if (list1.size() != list2.size())
            throw new IllegalArgumentException();
        Iterator<F> iterator1 = list1.iterator();
        Iterator<S> iterator2 = list2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            F first = iterator1.next();
            S second = iterator2.next();
            if (!biPredicate.test(first, second)) {
                return false;
            }
        }
        return true;
    }

    public static <K, V> Map<K, V> transListToMap(List<V> list,
                                                  String mapKeyName,
                                                  Class<V> mapValueClass) {
        if (mapValueClass == null || isEmpty(mapKeyName))
            throw new NullPointerException();
        if (isEmpty(list))
            return new HashMap<>();
        Map<K, V> map = new LinkedHashMap<>(list.size());
        for (V item : list) {
            K key = Reflect.on(item).get(mapKeyName);
            map.put(key, item);
        }
        return map;
    }
}