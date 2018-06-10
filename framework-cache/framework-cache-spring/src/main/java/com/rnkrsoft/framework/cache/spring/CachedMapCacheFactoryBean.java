package com.rnkrsoft.framework.cache.spring;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.cache.client.CacheClientSetting;
import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.cache.client.RedisType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;

/**
 * Created by rnkrsoft.com on 2018/5/20.
 */
public class CachedMapCacheFactoryBean implements FactoryBean<Cache>, BeanNameAware, InitializingBean {
    @Getter
    @Setter
    protected String beanName = "default";
    protected Cache cache;
    @Setter
    boolean allowNullValues;
    @Setter
    String prefix;
    @Setter
    RedisType redisType = RedisType.AUTO;
    @Setter
    String[] hosts;
    @Setter
    String password;
    @Setter
    int connectionTimeout;
    @Setter
    int soTimeout;
    @Setter
    int maxRedirections;
    @Setter
    int databaseIndex;


    @Override
    public Cache getObject() throws Exception {
        if (this.cache == null){
            throw ErrorContextFactory.instance().message("init cachedMap is failure!").runtimeException();
        }
        return this.cache;
    }

    @Override
    public Class<?> getObjectType() {
        return CachedMapCache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CacheClientSetting.CacheClientSettingBuilder builder = CacheClientSetting.builder();
        builder.redisType(redisType)
                .password(password)
                .connectionTimeout(connectionTimeout)
                .soTimeout(soTimeout)
                .maxRedirections(maxRedirections)
                .databaseIndex(databaseIndex);
        for(String host : hosts){
            builder.host(host);
        }
        CacheClient client = new CacheClient();
        client.init(builder.build());
        CachedMap cachedMap=  client.get(prefix);
        this.cache = new CachedMapCache(this.allowNullValues, this.beanName, cachedMap);
    }
}
