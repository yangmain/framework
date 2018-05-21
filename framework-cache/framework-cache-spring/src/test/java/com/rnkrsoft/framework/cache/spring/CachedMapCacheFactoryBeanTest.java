package com.rnkrsoft.framework.cache.spring;

import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.test.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/19.
 */
@ContextConfiguration("classpath*:CachedMapCacheFactoryBeanTest.xml")
public class CachedMapCacheFactoryBeanTest extends SpringTest {
    @Autowired
    CacheManager cacheManager;

    @Test
    public void testGetObject() throws Exception {
        System.out.println(cacheManager);
        Cache cache = cacheManager.getCache("test1");
        Cache.ValueWrapper valueWrapper = cache.get("xxxxxxxxxxx");
        System.out.println(valueWrapper);
        cache.clear();
        cache.put("xxxxxxxxxxx", "xxxx");
        Object val2 = cache.get("xxxxxxxxxxx").get();
        Object val3 = cache.get("xxxxxxxxxxx", String.class);
        System.out.println(valueWrapper.get());
        System.out.println(val2);
        System.out.println(val3);
    }
}