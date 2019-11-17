package com.yuyuko.mall.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class CacheUtils {
    /**
     * 缓存失效时找到对应失效的id来进行数据库批量查询，然后将查询到的数据插入回缓存的通用函数
     *
     * @param caches                 缓存集合，集合元素为null表示未命中
     * @param ids                    id集合
     * @param batchSelectFunc        由缓存集合中未命中的id集合来查找数据的函数
     * @param dataMapToCacheFunc     数据映射到缓存的函数
     * @param consumeMissedCacheFunc 额外作用于未命中缓存的函数，可能为null
     * @param <Cache>                缓存类型
     * @param <Id>                   id类型
     * @param <Data>                 数据类型
     */
    public static <Cache, Id, Data> List<Cache> handleBatchCache(
            List<Cache> caches,
            List<Id> ids,
            Function<List<Id>, List<Data>> batchSelectFunc,
            Function<List<Data>, List<Cache>> dataMapToCacheFunc,
            BiConsumer<List<Id>, List<Cache>> consumeMissedCacheFunc) {
        if (CollectionUtils.isEmpty(caches))
            return new ArrayList<>();
        if (ids == null || batchSelectFunc == null || dataMapToCacheFunc == null)
            throw new NullPointerException();
        if (caches.size() != ids.size())
            throw new IllegalArgumentException();
        List<Id> missedIds = new ArrayList<>();
        ListIterator<Cache> cachesIter = caches.listIterator();
        List<Integer> missedIdIndices = new ArrayList<>();
        while (cachesIter.hasNext()) {
            if (cachesIter.next() == null) {
                int index = cachesIter.previousIndex();
                missedIdIndices.add(index);
                missedIds.add(ids.get(index));
            }
        }

        if (missedIds.size() > 0) {
            List<Data> data = batchSelectFunc.apply(missedIds);
            List<Cache> missCaches = dataMapToCacheFunc.apply(data);
            CollectionUtils.consume(missedIdIndices, missCaches, caches::set);
            if (consumeMissedCacheFunc != null)
                consumeMissedCacheFunc.accept(missedIds, missCaches);
        }

        return caches;
    }

    /**
     * 缓存失效时找到对应失效的id来进行数据库查询，然后将查询到的数据插入回缓存的通用函数
     *
     * @param cache                  缓存，null表示未命中
     * @param id                     id
     * @param selectFunc             由id来查找数据的函数
     * @param dataMapToCacheFunc     数据映射到缓存的函数
     * @param consumeMissedCacheFunc 额外作用于未命中缓存的函数 可能为null
     * @param <Cache>                缓存类型
     * @param <Id>                   id类型
     * @param <Data>                 数据类型
     */
    public static <Cache, Id, Data> Cache handleCache(
            Cache cache,
            Id id,
            Function<Id, Data> selectFunc,
            Function<Data, Cache> dataMapToCacheFunc,
            BiConsumer<Id, Cache> consumeMissedCacheFunc
    ) {
        if (cache != null)
            return cache;
        if (id == null || selectFunc == null || dataMapToCacheFunc == null)
            throw new NullPointerException();
        Data data = selectFunc.apply(id);
        Cache cac = dataMapToCacheFunc.apply(data);
        if (consumeMissedCacheFunc != null)
            consumeMissedCacheFunc.accept(id, cac);
        return cac;
    }
}
