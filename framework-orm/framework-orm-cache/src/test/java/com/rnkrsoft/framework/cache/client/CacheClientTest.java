package com.rnkrsoft.framework.cache.client;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/5/18.
 */
public class CacheClientTest {

    @Test
    public void testInit() throws Exception {
        CacheClient client = new CacheClient();
        CacheClientSetting setting = CacheClientSetting.builder().redisType(RedisType.STANDALONE).hosts("118.24.45.37:2056").password("rnkrsoft").build();
        client.init(setting);
        CachedMap<String, Object> map = client.get("test");
//        for (int i = 0; i < 1000; i++) {
//            map.put(UUID.randomUUID().toString(), "1");
//        }
        map.clear();
        System.out.println(map.keys("*"));
        client.close();
    }

    @Test
    public void testGet() throws Exception {
        CacheClient client = new CacheClient();
        CacheClientSetting setting = CacheClientSetting.builder().hosts("127.0.0.1:6379,").build();
        client.init(setting);
        CachedMap<String, Object> map = client.get("test");
        map.size();
    }
}