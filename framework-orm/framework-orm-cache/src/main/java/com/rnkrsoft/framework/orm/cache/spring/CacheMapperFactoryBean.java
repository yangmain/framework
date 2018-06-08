package com.rnkrsoft.framework.orm.cache.spring;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.orm.cache.proxy.CacheProxy;
import com.rnkrsoft.framework.orm.cache.proxy.CacheProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by rnkrsoft.com on 2018/6/7.
 */
public class CacheMapperFactoryBean<T> implements InitializingBean, FactoryBean<T> {
    Class<T> mapperInterface;

    CacheProxyFactory<T> cacheProxyFactory;

    public CacheMapperFactoryBean(Class<T> mapperInterface, CacheClient cacheClient) {
        this.mapperInterface = mapperInterface;
        this.cacheProxyFactory = new CacheProxyFactory<T>(mapperInterface, cacheClient);
    }

    @Override
    public T getObject() throws Exception {
        return  this.cacheProxyFactory.newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
