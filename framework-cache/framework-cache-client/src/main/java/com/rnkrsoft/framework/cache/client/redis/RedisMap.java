package com.rnkrsoft.framework.cache.client.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.cache.client.CachedMap;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/5/18.
 */
public abstract class RedisMap<K, V> implements CachedMap<K, V> {
    protected static Gson GSON = new GsonBuilder().serializeNulls().setDateFormat("yyyyMMddHHmmssSSS").create();
    /**
     * 过滤条件
     */
    protected String prefix;
    protected CacheClient client;

    @Override
    public Class getNativeClass(String key) {
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix  + "_"+ cacheKey;
        }
        String oldVal = get(cacheKey);
        if (oldVal != null) {
            if (isPrimitive(oldVal)) {
                return Long.class;
            } else if (isWrapper(oldVal)) {
                try {
                    Wrapper oldWrapper = GSON.fromJson(oldVal, Wrapper.class);
                    Class clazz = Class.forName(oldWrapper.className);
                    return clazz;
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                } finally {
                }
            }
        }
        return String.class;
    }


    boolean isPrimitive(String value) {
        char[] chars = value.toCharArray();
        for (char c : chars) {
            if (c >= '0' && c <= '9' || c == '-') {

            } else {
                return false;
            }
        }
        return true;
    }

    boolean isWrapper(String value) {
        if (value == null) {
            return false;
        }
        String json = value.trim();
        return json.contains("\"serialVersionUID\"");
    }


}
