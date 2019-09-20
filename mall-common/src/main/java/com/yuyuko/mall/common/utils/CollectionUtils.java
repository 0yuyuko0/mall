package com.yuyuko.mall.common.utils;

import org.joor.Reflect;

import java.util.*;
import java.util.function.*;

public class CollectionUtils {
    private static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static <F, T> List<T> convertTo(List<F> list, Function<F, T> func) {
        if (isEmpty(list))
            return new ArrayList<>();
        List<T> newList = new ArrayList<>(list.size());
        list.forEach(item -> newList.add(func.apply(item)));
        return newList;
    }

    public static <F, S> void consume(List<F> list1, List<S> list2,
                                      BiConsumer<F, S> biConsumer) {
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

    public static <F, S, R> List<R> convertTo(List<F> list1, List<S> list2,
                                              BiFunction<F, S, R> biFunction) {
        if (isEmpty(list1) && isEmpty(list2))
            return new ArrayList<>();
        if (list1.size() != list2.size())
            throw new IllegalArgumentException();
        List<R> res = new ArrayList<>(list1.size());
        Iterator<F> iterator1 = list1.iterator();
        Iterator<S> iterator2 = list2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            F first = iterator1.next();
            S second = iterator2.next();
            res.add(biFunction.apply(first, second));
        }
        return res;
    }

    /**
     * list1中符合predicate条件的元素的下标对应的list2的元素再作用以batchFindFunc函数
     * 再用replaceFunc替换掉list1中符合条件的元素
     *
     * @param list          测试的集合
     * @param idList        测试成功时根据下标寻找的集合 通常是idList
     * @param predicate     测试谓词
     * @param batchFindFunc 通过idList来批量查找类型R的list
     * @param replaceFunc   根据类型R的list以及类型U的list的辅助信息（通常是排序）来替换list中的符合条件的元素
     * @param consumer      额外作用于list中符合条件的元素的函数
     * @param <T>           list的类型
     * @param <U>           idList的类型
     * @param <R>           转换后的类型
     */
    public static <T, U, R> void findAndExecBatchAndReplace(
            List<T> list,
            List<U> idList,
            Predicate<T> predicate,
            Function<List<U>, List<R>> batchFindFunc,
            BiFunction<List<R>, List<U>, List<T>> replaceFunc,
            BiConsumer<List<U>, List<T>> consumer) {
        if (isEmpty(list) || isEmpty(idList) || predicate == null || batchFindFunc == null)
            throw new NullPointerException();
        if (list.size() != idList.size())
            throw new IllegalArgumentException();
        List<U> arg = new ArrayList<>();
        ListIterator<T> iterator = list.listIterator();
        List<Integer> indexList = new ArrayList<>();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                int index = iterator.previousIndex();
                indexList.add(index);
                arg.add(idList.get(index));
            }
        }
        if (arg.size() > 0) {
            List<R> findRes = batchFindFunc.apply(arg);
            List<T> replaceList = replaceFunc.apply(findRes, arg);
            consume(indexList, replaceList, list::set);
            consumer.accept(arg, replaceList);
        }
    }

    public static <K, V> Map<K, V> convertListToMap(List<V> list,
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

    public static <K, T, V> Map<K, V> convertListToMap(List<T> list,
                                                       String mapKeyName,
                                                       String mapValueName) {
        if (isEmpty(mapKeyName) || isEmpty(mapValueName))
            throw new NullPointerException();
        if (isEmpty(list))
            return new HashMap<>();
        Map<K, V> map = new LinkedHashMap<>(list.size());
        for (T item : list) {
            Reflect reflect = Reflect.on(item);
            K key = reflect.get(mapKeyName);
            V value = reflect.get(mapValueName);
            map.put(key, value);
        }
        return map;
    }
}
