package com.rnkrsoft.framework.cache.spring;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.cache.client.CacheClientSetting;
import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.cache.client.RedisType;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by rnkrsoft.com on 2018/5/15.
 * CachedMap工厂对象
 */
@Setter
public class CachedMapFactoryBean implements FactoryBean<CachedMap> {
    String prefix;
    RedisType redisType = RedisType.AUTO;
    String[] hosts;
    String password;
    int connectionTimeout;
    int soTimeout;
    int maxRedirections;
    int databaseIndex;


    @Override
    public CachedMap getObject() throws Exception {
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
        return client.get(prefix);
    }

    @Override
    public Class<?> getObjectType() {
        return CachedMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
