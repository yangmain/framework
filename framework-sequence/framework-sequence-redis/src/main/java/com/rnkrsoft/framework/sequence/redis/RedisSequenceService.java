package com.rnkrsoft.framework.sequence.redis;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.redis.clients.jedis.Jedis;
import com.rnkrsoft.framework.sequence.redis.clients.jedis.JedisPool;
import com.rnkrsoft.framework.sequence.redis.clients.jedis.JedisPoolConfig;
import com.rnkrsoft.framework.sequence.redis.clients.jedis.Protocol;
import com.rnkrsoft.framework.sequence.redis.clients.util.Pool;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by rnkrsoft.com on 2018/4/23.
 * 基于Redis的序号生成方案
 */
@Slf4j
public class RedisSequenceService implements SequenceService {
    @Setter
    String host = "localhost";
    @Setter
    int port = 6379;
    @Setter
    int database = Protocol.DEFAULT_DATABASE;
    @Setter
    int timeout = 30;
    @Setter
    String password;
    protected Pool<Jedis> connectionPool = null;
    protected JedisPoolConfig connectionPoolConfig = new JedisPoolConfig();

    protected Jedis acquireConnection() {
        if (this.connectionPool == null) {
            this.connectionPool = new JedisPool(this.connectionPoolConfig, host, port, timeout, password, database);
        }
        Jedis jedis = null;
        try {
            jedis = connectionPool.getResource();
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .message("connect to redis server {}:{} happens error!", host, port)
                    .solution("1.确认redis服务器是否正常;2.用户名和密码是否正确")
                    .runtimeException();
        }
        if (database != 0) {
            jedis.select(database);
        }
        return jedis;
    }

    protected String safeString(String value) {
        return (value == null ? "" : value);
    }

    @Override
    public long nextval(String schema, String prefix, String sequenceName, String feature) {
        String key = safeString(schema) + "|" + safeString(prefix) + "|" + safeString(sequenceName) + "|" + safeString(feature);
        Jedis jedis = acquireConnection();
        try {
            if (log.isDebugEnabled()){
                log.debug("schema '{}', prefix '{}',sequence name '{}', feature '{}' ", schema, prefix, sequenceName, feature);
            }
            return jedis.incr(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    @Override
    public long curval(String schema, String prefix, String sequenceName, String feature) {
        String key = safeString(schema) + "|" + safeString(prefix) + "|" + safeString(sequenceName) + "|" + safeString(feature);
        Jedis jedis = acquireConnection();
        try {
            String val = jedis.get(key);
            if (val == null) {
                jedis.set(key, "0");
                val = "0";
            }
            return Long.valueOf(val);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
