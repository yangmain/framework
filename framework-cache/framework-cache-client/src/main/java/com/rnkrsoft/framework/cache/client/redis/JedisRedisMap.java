package com.rnkrsoft.framework.cache.client.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.params.set.SetParams;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/5/19.
 */
@Slf4j
public abstract class JedisRedisMap<K, V> extends RedisMap<K, V> {
    protected abstract Jedis getJedis();

    @Override
    public Class getNativeClass(K key) {
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        Jedis jedis = getJedis();
        String oldVal = jedis.get(cacheKey);
        if (oldVal != null) {
            if (isPrimitive(oldVal)) {
                return Long.class;
            } else if (isWrapper(oldVal)) {
                try {
                    Wrapper oldWrapper = Wrapper.valueOf(oldVal);
                    Class clazz = Class.forName(oldWrapper.className);
                    return clazz;
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                } finally {
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> keys(String pattern) {
        if (StringUtils.isNotBlank(prefix)) {
            pattern = prefix + "_" + pattern;
        }
        Jedis jedis = getJedis();
        try {
            return jedis.keys(pattern);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }

    }


    @Override
    public void expire(String key, int seconds) {
        Jedis jedis = getJedis();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            jedis.expire(key, seconds);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public void presist(String key) {
        Jedis jedis = getJedis();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            jedis.persist(key);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public long ttl(String key) {
        Jedis jedis = getJedis();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            return jedis.ttl(key);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public long incr(String key) {
        Jedis jedis = getJedis();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            return jedis.incr(key);
        } catch (JedisDataException e) {
            throw new RuntimeException("key:" + key + " 数据类型不为long或int!", e);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public long decr(String key) {
        Jedis jedis = getJedis();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        try {
            return jedis.decr(key);
        } catch (JedisDataException e) {
            throw new RuntimeException("key:" + key + " 数据类型不为long或int!", e);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }


    @Override
    public int size() {
        Jedis jedis = getJedis();
        try {
            return jedis.dbSize().intValue();
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
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
        Jedis jedis = getJedis();
        try {
            return jedis.exists(cacheKey);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }


    @Override
    public <T> T get(String key) {
        return (T) get0(key);
    }

    @Override
    public V get(Object key) {
        return (V) get0(key);
    }


    Object get0(Object key) {
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        Jedis jedis = getJedis();
        Object oldValue = null;
        try {
            String oldVal = jedis.get(cacheKey);
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
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public V put(K key, V value) {
        Class clazz = value == null ? Object.class : value.getClass();
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        Jedis jedis = getJedis();
        Object oldValue = null;
        try {
            String val = null;
            String oldVal = jedis.get(cacheKey);
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
            jedis.set(cacheKey, val);
            if (oldValue != null && oldVal != null) {
                try {
                    Wrapper oldWrapper = new Wrapper(oldVal);
                    oldValue = oldWrapper.get();
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                } finally {
                }
            }
            return (V) oldValue;
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }


    @Override
    public <T> T put(String key, Object value, int seconds, Class clazz) {
        Jedis jedis = getJedis();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        Object oldValue = null;
        try {
            String val = null;
            String oldVal = jedis.get(key);
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
            jedis.set(key, val, params);
            if (oldValue != null && oldVal != null) {
                try {
                    Wrapper oldWrapper = new Wrapper(oldVal);
                    oldValue = oldWrapper.get();
                } catch (ClassNotFoundException e) {
                    System.err.println(e);
                } finally {
                }
            }
            return (T) oldValue;
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public <T> T put(String key, Object value, int seconds) {
        Class clazz = value == null ? Object.class : value.getClass();
        if (StringUtils.isNotBlank(prefix)) {
            key = prefix + "_" + key;
        }
        Jedis jedis = getJedis();
        Object oldValue = null;
        try {
            String val = null;
            String oldVal = jedis.get(key);
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
                jedis.set(key, val, params);
            } else {
                jedis.set(key, val);
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
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public V remove(Object key) {
        String cacheKey = key == null ? null : key.toString();
        if (StringUtils.isNotBlank(prefix)) {
            cacheKey = prefix + "_" + cacheKey;
        }
        Jedis jedis = getJedis();
        Object oldValue = null;
        try {
            String oldVal = null;
            if (jedis.exists(cacheKey)) {
                oldVal = jedis.get(cacheKey);
            }
            jedis.del(cacheKey);
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
            return (V) oldValue;
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }


    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Jedis jedis = getJedis();
        try {
            for (K key : m.keySet()) {
                String cacheKey = key == null ? null : key.toString();
                if (StringUtils.isNotBlank(prefix)) {
                    cacheKey = prefix + "_" + cacheKey;
                }
                Object value = m.get(key);
                try {
                    String className = value == null ? Object.class.getName() : value.getClass().getName();
                    String val = null;
                    if (value == null) {
                        val = null;
                    } else {
                        Class clazz = value.getClass();
                        if (clazz == Integer.TYPE || clazz == Integer.class) {
                            val = String.valueOf(value);
                        } else if (clazz == Long.TYPE || clazz == Long.class) {
                            val = String.valueOf(value);
                        } else {
                            Wrapper oldWrapper = new Wrapper(value);
                            val = oldWrapper.toString();
                        }
                    }
                    jedis.set(cacheKey, val);
                } finally {
                    if (log.isDebugEnabled()) {
                        log.debug("set key :{} into redis data:{}", key, value);
                    }
                }
            }
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public void clear() {
        Jedis jedis = getJedis();
        try {
            if (prefix == null) {
                jedis.flushDB();
            } else {
                Set<String> keys = keys("*");
                jedis.del(keys.toArray(new String[keys.size()]));
            }
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Redis not support containsValue(value)");
    }

    @Override
    public Set<K> keySet() {
        Jedis jedis = getJedis();
        String pattern = "*";
        if (StringUtils.isNotBlank(prefix)) {
            pattern = prefix + "_" + pattern;
        }
        Set<K> keys1 = new HashSet();
        try {
            log.warn("SentinelMap 使用 keySet 可能导致系统溢出 ");
            Set<String> keys = jedis.keys(pattern);
            for (String key : keys) {
                if (key.length() < prefix.length()) {
                    continue;
                }
                String newKey = key.substring(prefix.length());
                keys1.add((K) newKey);
            }
            return keys1;
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }
}
