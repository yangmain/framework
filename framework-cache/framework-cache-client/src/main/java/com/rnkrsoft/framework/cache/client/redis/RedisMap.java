package com.rnkrsoft.framework.cache.client.redis;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.cache.client.CachedMap;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;


/**
 * Created by rnkrsoft.com on 2018/5/18.
 */
public abstract class RedisMap<K, V> implements CachedMap<K, V> {
    /**
     * 过滤条件
     */
    protected String prefix;
    protected CacheClient client;


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
