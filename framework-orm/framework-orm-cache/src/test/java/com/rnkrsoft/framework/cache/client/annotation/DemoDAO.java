package com.rnkrsoft.framework.cache.client.annotation;

import com.rnkrsoft.framework.orm.cache.*;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Cache(expire = 6000, index = 1)
public interface DemoDAO extends CacheInterface {
    @Set
    void set(String key, DemoEntity user);

    @Get
    DemoEntity get(String key);

    @GetSet(expire = 6000)
    DemoEntity getSet(String key, DemoEntity user);

    @Expire
    void expire(String key);

    @Persist
    void presist(String key);

    @Ttl
    Long ttl(String key);

    @Incr(increment = 2)
    Long incr(String key);

    @Decr(decrement = 3)
    Long decr(String key);

    @Keys
    java.util.Set<String> keys(String pattern);
}
