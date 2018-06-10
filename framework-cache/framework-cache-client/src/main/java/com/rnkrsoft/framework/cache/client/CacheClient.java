package com.rnkrsoft.framework.cache.client;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.cache.client.redis.ClusterMap;
import com.rnkrsoft.framework.cache.client.redis.SentinelMap;
import com.rnkrsoft.framework.cache.client.redis.StandaloneMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by rnkrsoft.com on 2018/5/15.
 */
@Slf4j
public class CacheClient {
    static final Lock LOCK = new ReentrantLock();
    @Getter
    volatile JedisPool jedisPool;
    @Getter
    volatile JedisSentinelPool sentinelPool;
    @Getter
    volatile JedisCluster jedisCluster;

    GenericObjectPoolConfig poolConfig;
    CacheClientSetting setting;

    public CacheClient init(CacheClientSetting setting) {
        this.poolConfig = new GenericObjectPoolConfig();
        this.poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 3);
        this.poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        this.poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
        this.poolConfig.setMaxWaitMillis(1000L);
        this.poolConfig.setJmxNamePrefix("jedis-pool");
        this.poolConfig.setJmxEnabled(true);
        this.setting = setting;
        try {
            if (setting.redisType == RedisType.AUTO) {
                try {
                    log.info("try to connect standalone redis instance...");
                    JedisPool instance = initStandalone(setting);
                    this.jedisPool = instance;
                    this.setting.redisType = RedisType.STANDALONE;
                    return this;
                } catch (Exception e) {
                    log.warn("try to connect standalone redis instance fail");
                }
                try {
                    log.info("try to connect sentinel redis instance...");
                    JedisSentinelPool instance = initSentinel(setting);
                    this.sentinelPool = instance;
                    this.setting.redisType = RedisType.SENTINEL;
                    return this;
                } catch (Exception e) {
                    log.warn("try to connect sentinel redis instance fail");
                }
                try {
                    log.info("try to connect cluster redis instance...");
                    JedisCluster instance = initCluster(setting);
                    this.jedisCluster = instance;
                    this.setting.redisType = RedisType.CLUSTER;
                    return this;
                } catch (Exception e) {
                    log.warn("try to connect cluster redis instance fail");
                }

                throw ErrorContextFactory.instance().message("auto detect redis fail").solution("please check redis instance type or instance is online").runtimeException();
            } else if (setting.redisType == RedisType.STANDALONE) {
                this.jedisPool = initStandalone(setting);
            } else if (setting.redisType == RedisType.SENTINEL) {
                this.sentinelPool = initSentinel(setting);
            } else if (setting.redisType == RedisType.CLUSTER) {
                this.jedisCluster = initCluster(setting);
            }
        } finally {
            log.info("cacheClient use {}", this.setting.redisType);
        }
        return this;
    }


    static Set<HostAndPort> convert(List<String> hosts) {
        Set<HostAndPort> nodes = new HashSet();
        for (String node : hosts) {
            String[] ipAndPort = node.split(":");
            if (ipAndPort.length < 2) {
                throw ErrorContextFactory.instance().message("配置集群地址信息错误").runtimeException();
            }
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);
            nodes.add(new HostAndPort(ip, port));
        }
        return nodes;
    }

    JedisPool initStandalone(CacheClientSetting settings) {
        JedisPool jedisPool = null;
        List<String> hosts = settings.getHosts();
        int size = hosts.size();
        if (size == 0) {

        } else if (size > 1) {

        }
        String[] strings = hosts.get(0).split(":");
        String host = strings[0];
        int port = Integer.parseInt(strings[1]);
        while (true) {
            try {
                LOCK.tryLock(10, TimeUnit.MILLISECONDS);
                jedisPool = new JedisPool(poolConfig, host, port, settings.getSoTimeout(), settings.getPassword(), settings.getDatabaseIndex());
                break;
            } catch (Throwable e) {//容错
            } finally {
                LOCK.unlock();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));//活锁
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        return jedisPool;
    }

    JedisCluster initCluster(CacheClientSetting settings) {
        JedisCluster jedisCluster = null;
        Set<HostAndPort> hosts = convert(settings.getHosts());
        while (true) {
            try {
                LOCK.tryLock(10, TimeUnit.MILLISECONDS);
                jedisCluster = new JedisCluster(hosts, settings.getConnectionTimeout(), settings.getSoTimeout(), settings.getMaxRedirections(), poolConfig);
                break;
            } catch (Throwable e) {//容错
            } finally {
                LOCK.unlock();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));//活锁
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        return jedisCluster;
    }

    JedisSentinelPool initSentinel(CacheClientSetting setting) {
        JedisSentinelPool pool = null;
        while (true) {
            try {
                LOCK.tryLock(10, TimeUnit.MILLISECONDS);
                pool = new JedisSentinelPool("sentinel", new HashSet(setting.getHosts()), poolConfig, setting.getConnectionTimeout(), setting.getSoTimeout(), setting.getPassword(), setting.getDatabaseIndex());
                break;
            } catch (Throwable e) {//容错
            } finally {
                LOCK.unlock();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));//活锁
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            if (log.isDebugEnabled()) {
                log.debug("waiting for connect redis...");
            }
        }
        return pool;
    }


    public void close() {
        if (setting.redisType == RedisType.STANDALONE) {
            if (jedisPool != null && !jedisPool.isClosed()) {
                jedisPool.close();
            }
        } else if (setting.redisType == RedisType.SENTINEL) {
            if (sentinelPool != null && !sentinelPool.isClosed()) {
                sentinelPool.close();
            }
        } else if (setting.redisType == RedisType.CLUSTER) {
            if (jedisCluster != null) {
                jedisCluster.close();
            }
        } else {
            throw ErrorContextFactory.instance().message("不支持的Redis模式").runtimeException();
        }
    }

    public CachedMap get(String prefix) {
        if (setting.redisType == RedisType.STANDALONE) {
            if (jedisPool == null || jedisPool.isClosed()) {

            }
            return new StandaloneMap(prefix, this);
        } else if (setting.redisType == RedisType.SENTINEL) {
            if (sentinelPool == null || sentinelPool.isClosed()) {
                //TODO
            }
            return new SentinelMap(prefix, this);
        } else if (setting.redisType == RedisType.CLUSTER) {
            if (jedisCluster == null) {
                //TODO
            }
            return new ClusterMap(prefix, this);
        } else {
            throw ErrorContextFactory.instance().message("不支持的Redis模式").runtimeException();
        }
    }
}
