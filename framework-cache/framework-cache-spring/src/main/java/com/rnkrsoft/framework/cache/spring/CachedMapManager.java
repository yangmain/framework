package com.rnkrsoft.framework.cache.spring;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.serializer.support.SerializationDelegate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by rnkrsoft.com on 2018/5/20.
 */
public class CachedMapManager implements CacheManager, BeanClassLoaderAware {
    protected SerializationDelegate serialization;
    protected final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(100);

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.serialization = new SerializationDelegate(classLoader);
    }

    /**
     * 为这个CacheManager指定一个缓存名称集合（静态模式）。
     * <p> 一旦调用了这个方法，缓存的数量以及缓存的名称就全部固定了，在runtime就没法进行动态创建了。
     * <p>如果你在调用此方法时传递了一个null的参数，那么就会把模式设置为动态模式（dynamic），这时候就允许进一步再创建缓存了。
     *
     * @param caches 一个带有名称和自定义配置的缓存的集合
     */
    public void setCaches(List<Cache> caches) {
        if (caches != null) {
            for (Cache cache : caches) {
                this.cacheMap.put(cache.getName(), cache);
            }
        }
    }
    @Override
    public Cache getCache(String name) {
        return this.cacheMap.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }
}
