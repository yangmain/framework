package com.rnkrsoft.framework.orm.cache.spring;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.orm.cache.proxy.CacheProxyFactory;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by rnkrsoft.com on 2018/6/7.
 */
public class CacheMapperFactoryBean<T> implements InitializingBean, FactoryBean<T> {
    @Setter
    Class<T> cacheInterface;

    @Setter
    CacheClient cacheClient;

    public CacheMapperFactoryBean(Class<T> cacheInterface, CacheClient cacheClient) {
        this.cacheInterface = cacheInterface;
        this.cacheClient = cacheClient;
    }

    public CacheMapperFactoryBean() {
    }

    @Override
    public T getObject() throws Exception {
        CacheProxyFactory<T> cacheProxyFactory = new CacheProxyFactory<T>(cacheInterface, cacheClient);
        return cacheProxyFactory.newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return cacheInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
