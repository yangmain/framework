package com.rnkrsoft.framework.orm.cache.proxy;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.orm.cache.Cache;
import com.rnkrsoft.framework.orm.cache.CacheMapper;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 * 缓存代理工厂，将接口包装成DAO对象
 */
public class CacheProxyFactory<CacheDAO>{
    final Class<CacheDAO> cacheInterface;
    final CacheClient cacheClient;

    public CacheProxyFactory(Class<CacheDAO> cacheInterface, CacheClient cacheClient) {
        this.cacheInterface = cacheInterface;
        this.cacheClient = cacheClient;
        if (!Arrays.asList(cacheInterface.getInterfaces()).contains(CacheMapper.class)){
            throw ErrorContextFactory.instance().message("接口 '{}'不能作为Cache 数据访问对象", cacheInterface).runtimeException();
        }
    }

    public CacheDAO newInstance() {
        Cache cache = cacheInterface.getAnnotation(Cache.class);
        String cachePrefix = cacheInterface.getSimpleName();
        if (cache != null && !cache.prefix().isEmpty()){
            cachePrefix = cache.prefix();
        }
        CacheProxy<CacheDAO> cacheProxy = new CacheProxy<CacheDAO>(this.cacheClient.get(cachePrefix));
        return (CacheDAO) Proxy.newProxyInstance(cacheInterface.getClassLoader(), new Class[]{cacheInterface}, cacheProxy);
    }
}
