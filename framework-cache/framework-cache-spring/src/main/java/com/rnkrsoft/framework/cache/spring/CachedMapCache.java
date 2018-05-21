package com.rnkrsoft.framework.cache.spring;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.cache.client.CachedMap;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;

/**
 * Created by rnrksoft.com on 2018/5/20.
 */
public class CachedMapCache extends AbstractValueAdaptingCache {

    private final String name;

    CachedMap cachedMap;

    public CachedMapCache(boolean allowNullValues, String name, CachedMap cachedMap) {
        super(allowNullValues);
        this.name = name;
        this.cachedMap = cachedMap;
    }

    @Override
    protected Object lookup(Object key) {
        return cachedMap.get(key);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cachedMap;
    }

    @Override
    public ValueWrapper get(Object key) {
        return toValueWrapper(cachedMap.get(key));
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object val = cachedMap.get(key);
        if (val == null){
            return null;
        }else{
            Class clazz = val.getClass();
            if (clazz != type){
                throw ErrorContextFactory.instance().message("类型不支持").runtimeException();
            }
            return (T) val;
        }
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        throw new UnsupportedOperationException("not support async");
    }

    @Override
    public void put(Object key, Object val) {
        cachedMap.put(key, toStoreValue(val));
    }

    @Override
    public ValueWrapper putIfAbsent(final Object key, final Object val) {
        final Object old = cachedMap.put(key, toStoreValue(val));
        return new ValueWrapper() {
            @Override
            public Object get() {
                return old;
            }
        };
    }

    @Override
    public void evict(Object key) {
        cachedMap.remove(key);
    }

    @Override
    public void clear() {
        cachedMap.clear();
    }
}
