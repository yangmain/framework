package com.rnkrsoft.framework.cache.client.annotation;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.cache.client.CacheClientSetting;
import com.rnkrsoft.framework.cache.client.RedisType;
import com.rnkrsoft.framework.orm.cache.proxy.CacheProxyFactory;
import org.junit.Test;

/**
 * Created by woate on 2018/6/2.
 */
public class UserDAOTest {
    @Test
    public void test1(){
        CacheClient client = new CacheClient();
        CacheClientSetting setting = CacheClientSetting.builder().redisType(RedisType.AUTO).hosts("localhost:6379").build();
        client.init(setting);
        CacheProxyFactory<UserDAO> cacheProxyFactory = new CacheProxyFactory(UserDAO.class, client);
        UserDAO userDAO = cacheProxyFactory.newInstance();
        userDAO.set("sss", new User("xxx", 12, false));
        User user = userDAO.get("sss");
        System.out.println(user);
        long x = userDAO.incr("xxxxx");
        System.out.println(x);

        long y = userDAO.decr("yyyyy");
        System.out.println(y);

        java.util.Set<String> keys = userDAO.keys("*");
        System.out.println(keys);

        userDAO.expire("yyyyy");

        System.out.println(userDAO.ttl("yyyyy"));
    }
}
