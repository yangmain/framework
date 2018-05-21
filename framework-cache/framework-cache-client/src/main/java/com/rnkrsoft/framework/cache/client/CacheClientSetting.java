package com.rnkrsoft.framework.cache.client;

import com.devops4j.logtrace4j.ErrorContextFactory;
import lombok.Data;
import lombok.Getter;
import redis.clients.jedis.Protocol;

import java.util.*;

/**
 * Created by woate on 2018/5/16.
 */

public class CacheClientSetting {
    @Getter
    final List<String> hosts = new ArrayList();
    @Getter
    RedisType redisType = RedisType.SENTINEL;
    @Getter
    String password;
    /**
     * redis连接超时(单位:毫秒)
     */
    @Getter
    int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
    /**
     * jedis读写超时(单位:毫秒)
     */
    @Getter
    int soTimeout = Protocol.DEFAULT_TIMEOUT;
    /**
     * 节点定位重试次数:默认3次,集群模式有效
     */
    @Getter
    int maxRedirections = 3;
    /**
     * 数据库编号，默认0，总共16个
     */
    @Getter
    int databaseIndex = 0;

    private CacheClientSetting() {
    }

    public static CacheClientSettingBuilder builder(){
        return new CacheClientSettingBuilder();
    }

    public static class CacheClientSettingBuilder {
        final List<String> hosts = new ArrayList();

        String password;

        RedisType redisType = RedisType.SENTINEL;
        /**
         * redis连接超时(单位:毫秒)
         */
        int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
        /**
         * jedis读写超时(单位:毫秒)
         */
        int soTimeout = Protocol.DEFAULT_TIMEOUT;
        /**
         * 节点定位重试次数:默认3次,集群模式有效
         */
        int maxRedirections = 3;

        /**
         * 数据库编号，默认0，总共16个
         */
        @Getter
        int databaseIndex = Protocol.DEFAULT_DATABASE;

        private CacheClientSettingBuilder() {
        }

        public CacheClientSettingBuilder hosts(String hosts) {
            List<String> hosts0 = spilt(hosts);
            this.hosts.addAll(hosts0);
            return this;
        }

        public CacheClientSettingBuilder host(String host) {
            List<String> hosts0 = spilt(host);
            this.hosts.addAll(hosts0);
            return this;
        }

        public CacheClientSettingBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CacheClientSettingBuilder redisType(RedisType redisType) {
            this.redisType = redisType;
            return this;
        }

        public CacheClientSettingBuilder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public CacheClientSettingBuilder soTimeout(int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public CacheClientSettingBuilder maxRedirections(int maxRedirections) {
            this.maxRedirections = maxRedirections;
            return this;
        }

        public CacheClientSettingBuilder databaseIndex(int databaseIndex) {
            this.databaseIndex = databaseIndex;
            return this;
        }

        public CacheClientSetting build() {
            CacheClientSetting setting = new CacheClientSetting();
            setting.getHosts().addAll(hosts);
            setting.redisType = redisType;
            setting.password = password;
            setting.connectionTimeout = connectionTimeout;
            setting.soTimeout = soTimeout;
            setting.maxRedirections = maxRedirections;
            setting.databaseIndex = databaseIndex;
            return setting;
        }
    }

    static List<String> spilt(String value) {
        String[] values = value.split(",");
        for (String node : values) {
            String[] ipAndPort = node.split(":");
            if (ipAndPort.length < 2) {
                throw ErrorContextFactory.instance().message("配置地址'{}'错误", node).runtimeException();
            }
        }
        return Arrays.asList(values);
    }
}
