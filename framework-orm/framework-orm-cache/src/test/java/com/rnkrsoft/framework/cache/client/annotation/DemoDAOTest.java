package com.rnkrsoft.framework.cache.client.annotation;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.cache.client.CacheClientSetting;
import com.rnkrsoft.framework.cache.client.RedisType;
import com.rnkrsoft.framework.orm.cache.proxy.CacheProxyFactory;
import org.junit.Test;

/**
 * Created by woate on 2018/6/2.
 */
public class DemoDAOTest {
    @Test
    public void test1(){
        CacheClient client = new CacheClient();
        CacheClientSetting setting = CacheClientSetting.builder().redisType(RedisType.AUTO).hosts("localhost:6379").build();
        client.init(setting);
        CacheProxyFactory<DemoDAO> cacheProxyFactory = new CacheProxyFactory(DemoDAO.class, client);
        DemoDAO demoDAO = cacheProxyFactory.newInstance();
        demoDAO.set("sss", new DemoEntity("xxx", 12, false));
        DemoEntity user = demoDAO.get("sss");
        System.out.println(user);
        long x = demoDAO.incr("xxxxx");
        System.out.println(x);

        long y = demoDAO.decr("yyyyy");
        System.out.println(y);

        java.util.Set<String> keys = demoDAO.keys("*");
        System.out.println(keys);

        demoDAO.expire("yyyyy");

        System.out.println(demoDAO.ttl("yyyyy"));
    }
}
