package com.rnkrsoft.framework.cache.spring;

import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.test.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by rnkrsoft.com on 2018/5/19.
 */
@ContextConfiguration("classpath*:CachedMapFactoryBeanTest.xml")
public class CachedMapFactoryBeanTest extends SpringTest {

    @Autowired
    CachedMap<?, ?> cachedMap;

    @Test
    public void testGetObject() throws Exception {
        System.out.println(cachedMap.size());
        System.out.println(cachedMap.incr("ssss"));
    }
}