package com.rnkrsoft.framework.cache.client;

import java.util.Map;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/05/20.
 * 可缓存的Map
 */
public interface CachedMap<K,V> extends Map<K,V>{
    /**
     * 类似数据库的模糊查询
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 获得原始的类型
     * @param key
     * @return
     */
    Class getNativeClass(K key);

    <T> T get(String key);
    /**
     * 放入带过期事件的键值
     * @param key
     * @param val
     * @param seconds
     */
    <T> T put(String key, Object val, int seconds);

    /**
     * 放入带过期事件的键值
     * @param key
     * @param val
     * @param seconds
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T put(String key, Object val, int seconds, Class clazz);

    /**
     * 设置过期时间
     * @param key
     * @param seconds
     */
    void expire(String key, int seconds);

    /**
     * 查看剩余过期事件
     * @param key
     * @return
     */
    long ttl(String key);

    /**
     * 取消超时
     * @param key
     */
    void presist(String key);
    /**
     * 自增1
     * @param key
     * @return
     */
    long incr(String key);

    /**
     * 自减1
     * @param key
     * @return
     */
    long decr(String key);
}
