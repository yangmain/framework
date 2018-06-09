package com.rnkrsoft.framework.cache.client.redis;

import com.rnkrsoft.framework.cache.client.CacheClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.params.set.SetParams;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/5/18.
 */
@Slf4j
public class ClusterMap<K, V> extends RedisMap<K, V> {
    /**
     * 构建一个缓存，使用Map初始化
     *
     * @param prefix
     * @param client
     */
    public ClusterMap(String prefix, CacheClient client) {
        this.prefix = prefix;
        this.client = client;
    }


    @Override
    public Set<String> keys(String pattern) {
        if (StringUtils.isNotBlank(prefix)) {
            pattern = prefix + "_" + pattern;
        }
        JedisCluster jedisCluster = client.getJedisCluster();
        try {
            //TODO
            return jedisCluster.hkeys(pattern);
        } finally {

        }
    }

    @Override
    public <T> T get(String key) {
        return (T) get0(key);
    }


    Object get0(Object key) {
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        JedisCluster jedisCluster = client.getJedisCluster();
        Object oldValue = null;
        try {
            String oldVal = jedisCluster.get(cacheKey);
            if (oldVal != null) {
                if (isPrimitive(oldVal)) {
                    //NOOP
                    oldValue = Long.valueOf(oldVal);
                } else if (isWrapper(oldVal)) {
                    try {
                        Wrapper oldWrapper = Wrapper.valueOf(oldVal);
                        oldValue = oldWrapper.get();
                    } catch (ClassNotFoundException e) {
                        System.err.println(e);
                    } finally {
                    }
                } else {
                    oldValue = oldVal;
                }
            }
            return oldValue;
        } finally {
        }
    }


    @Override
    public V put(K key, V value) {
        Class clazz = value == null ? Object.class : value.getClass();
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        JedisCluster jedisCluster = client.getJedisCluster();
        Object oldValue = null;
        try {
            String val = null;
            String oldVal = jedisCluster.get(cacheKey);
            String className = clazz.getName();
            boolean primitive = false;
            if (oldVal != null) {
                primitive = isPrimitive(oldVal);
            }
            if (clazz == Integer.TYPE || clazz == Integer.class) {
                val = String.valueOf(value);
                if (oldVal != null && primitive) {
                    oldValue = Integer.valueOf(oldVal);
                }
            } else if (clazz == Long.TYPE || clazz == Long.class) {
                val = String.valueOf(value);
                if (oldVal != null && primitive) {
                    oldValue = Long.valueOf(oldVal);
                }
            } else {
                Wrapper wrapper = new Wrapper(value);
                val = wrapper.toString();
            }
            jedisCluster.set(cacheKey, val);
            if (oldValue == null && oldVal != null) {
                try {
                    Wrapper oldWrapper = Wrapper.valueOf(oldVal);
                    oldValue = oldWrapper.get();
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                } finally {
                }
            }
            return (V) oldValue;
        } finally {

        }
    }

    @Override
    public <T> T put(String key, Object value, int seconds) {
        Class clazz = value == null ? Object.class : value.getClass();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        JedisCluster jedisCluster = client.getJedisCluster();
        Object oldValue = null;
        try {
            String val = null;
            String oldVal = jedisCluster.get(key);
            String className = clazz.getName();
            boolean primitive = false;
            if (oldVal != null) {
                primitive = isPrimitive(oldVal);
            }
            if (clazz == Integer.TYPE || clazz == Integer.class) {
                val = String.valueOf(value);
                if (oldVal != null && primitive) {
                    oldValue = Integer.valueOf(oldVal);
                }
            } else if (clazz == Long.TYPE || clazz == Long.class) {
                val = String.valueOf(value);
                if (oldVal != null && primitive) {
                    oldValue = Long.valueOf(oldVal);
                }
            } else {
                Wrapper wrapper = new Wrapper(value);
                val = wrapper.toString();
            }
            if (seconds > 0) {
                SetParams params = SetParams.setParams();
                params.ex(seconds);
                jedisCluster.set(key, val, params);
            } else {
                jedisCluster.set(key, val);
            }
            if (oldValue == null && oldVal != null) {
                try {
                    Wrapper oldWrapper = Wrapper.valueOf(oldVal);
                    oldValue = oldWrapper.get();
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                } finally {
                }
            }
            return (T) oldValue;
        } finally {

        }
    }

    @Override
    public <T> T put(String key, Object value, int seconds, Class clazz) {
        JedisCluster jedisCluster = client.getJedisCluster();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        Object oldValue = null;
        try {
            String val = null;
            String oldVal = jedisCluster.get(key);
            String className = clazz.getName();
            boolean primitive = false;
            if (oldVal != null) {
                primitive = isPrimitive(oldVal);
            }
            if (clazz == Integer.TYPE || clazz == Integer.class) {
                val = String.valueOf(value);
                if (oldVal != null && primitive) {
                    oldValue = Integer.valueOf(oldVal);
                }
            } else if (clazz == Long.TYPE || clazz == Long.class) {
                val = String.valueOf(value);
                if (oldVal != null && primitive) {
                    oldValue = Long.valueOf(oldVal);
                }
            } else {
                Wrapper wrapper = new Wrapper(value);
                val = wrapper.toString();
            }
            SetParams params = SetParams.setParams();
            params.ex(seconds);
            jedisCluster.set(key, val, params);
            if (oldValue == null && oldVal != null) {
                try {
                    Wrapper oldWrapper = Wrapper.valueOf(oldVal);
                    oldValue = oldWrapper.get();
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                } finally {
                }
            }
            return (T) oldValue;
        } finally {

        }
    }

    @Override
    public void expire(String key, int seconds) {
        JedisCluster jedisCluster = client.getJedisCluster();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            jedisCluster.expire(key, seconds);
        } finally {

        }
    }
    @Override
    public void presist(String key) {
        JedisCluster jedisCluster = client.getJedisCluster();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            jedisCluster.persist(key);
        } finally {

        }
    }
    @Override
    public long ttl(String key) {
        JedisCluster jedisCluster = client.getJedisCluster();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            return jedisCluster.ttl(key);
        } finally {

        }
    }

    @Override
    public long incr(String key) {
        JedisCluster jedisCluster = client.getJedisCluster();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            return jedisCluster.incr(key);
        } catch (JedisDataException e) {
            throw new RuntimeException("key:" + key + " 数据类型不为long或int!", e);
        } finally {

        }
    }

    @Override
    public long decr(String key) {
        JedisCluster jedisCluster = client.getJedisCluster();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            return jedisCluster.decr(key);
        } catch (JedisDataException e) {
            throw new RuntimeException("key:" + key + " 数据类型不为long或int!", e);
        } finally {

        }
    }

    @Override
    public int size() {
        return keySet().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        JedisCluster jedisCluster = client.getJedisCluster();
        try {
            return jedisCluster.exists(cacheKey);
        } finally {

        }
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Redis not support containsValue(value)");
    }

    @Override
    public V get(Object key) {
        return (V) get0(key);
    }


    @Override
    public V remove(Object key) {
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        JedisCluster jedisCluster = client.getJedisCluster();
        Object oldValue = null;
        try {
            String oldVal = null;
            if (jedisCluster.exists(cacheKey)) {
                oldVal = jedisCluster.get(cacheKey);
            }
            jedisCluster.del(cacheKey);
            if (oldVal != null) {
                if (isPrimitive(oldVal)) {
                    //NOOP
                    oldValue = Long.valueOf(oldVal);
                } else if (isWrapper(oldVal)) {
                    try {
                        Wrapper oldWrapper = new Wrapper(oldVal);
                        oldValue = oldWrapper.get();
                    } catch (ClassNotFoundException e) {
                        System.err.println(e);
                    } finally {
                    }
                } else {
                    oldValue = oldVal;
                }
            }
            return (V) oldValue;
        } finally {

        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public void clear() {
        JedisCluster jedisCluster = client.getJedisCluster();
        try {
            if (prefix == null) {
//                jedis.flushDB();
            } else {
                Set<String> keys = keys("*");
                jedisCluster.del(keys.toArray(new String[keys.size()]));
            }
        } finally {
        }
    }

    @Override
    public Set<K> keySet() {
        JedisCluster jedisCluster = client.getJedisCluster();
        String pattern = "*";
        if (StringUtils.isNotBlank(prefix)) {
            pattern = prefix + "_" + pattern;
        }
        Set<K> keys1 = new HashSet();
        try {
            log.warn("SentinelMap 使用 keySet 可能导致系统溢出 ");
            Set<String> keys = jedisCluster.hkeys(pattern);
            for (String key : keys) {
                if (key.length() < prefix.length()) {
                    continue;
                }
                String newKey = key.substring(prefix.length());
                keys1.add((K) newKey);
            }
            return keys1;
        } finally {

        }
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Redis not support values()");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Redis not support entrySet()");
    }
}
