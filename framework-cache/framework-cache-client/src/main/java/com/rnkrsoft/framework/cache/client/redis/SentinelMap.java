package com.rnkrsoft.framework.cache.client.redis;

import com.rnkrsoft.framework.cache.client.CacheClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.params.set.SetParams;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/4/20.
 * 将缓存代理为Map使用
 */
@Slf4j
@Getter
public class SentinelMap<K, V> extends JedisRedisMap<K, V> {


    /**
     * 构建一个缓存，使用Map初始化
     *
     * @param prefix
     * @param client
     */
    public SentinelMap(String prefix, CacheClient client) {
        this.prefix = prefix;
        this.client = client;
    }

    protected Jedis getJedis(){
        Jedis jedis = client.getSentinelPool().getResource();
        return jedis;
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
