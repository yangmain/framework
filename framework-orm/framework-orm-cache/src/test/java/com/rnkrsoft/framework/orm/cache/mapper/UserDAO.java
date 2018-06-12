package com.rnkrsoft.framework.orm.cache.mapper;

import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.orm.cache.*;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Cache(expire = 6000, index = 1, prefix = "")
public interface UserDAO extends CacheMapper{
    @Set
    void set(String key, User user);

    @Get
    User get(String key);

    @Get
    Long get1(String key);

    @GetSet(expire = 100)
    User getSet(String key, User user);

    @Expire
    void expire(String key, int second);

    @Presist
    void presist(String key);

    @Ttl
    Long ttl(String key);

    @Incr(increment = 2)
    Long incr(String key);

    @Decr(decrement = 3)
    Long decr(String key);

    @Keys
    java.util.Set<String> keys(String pattern);

    @Type
    Class type(String key);
    CachedMap get();
}
