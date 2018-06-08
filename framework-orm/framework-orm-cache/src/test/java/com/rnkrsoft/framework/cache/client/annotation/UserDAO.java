package com.rnkrsoft.framework.cache.client.annotation;

import com.rnkrsoft.framework.orm.cache.*;

/**
 * Created by woate on 2018/6/2.
 */
@Cache(expire = 6000, db = 1)
public interface UserDAO extends CacheMapper {
    @Set
    void set(String key, User user);

    @Get
    User get(String key);

    @GetSet(expire = 6000)
    User getSet(String key, User user);

    @Expire(seconds = 6000)
    void expire(String key);

    @Presist
    void presist(String key);

    @Ttl
    long ttl(String key);

    @Incr(increment = 2)
    long incr(String key);

    @Decr(decrement = 3)
    long decr(String key);

    @Keys
    java.util.Set<String> keys(String pattern);
}
