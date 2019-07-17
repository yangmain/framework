package com.rnkrsoft.framework.orm.cache.mapper;

import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.orm.cache.*;
import com.rnkrsoft.framework.orm.cache.entity.User;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Cache(expire = 6000, index = 1, prefix = "")
public interface UserDAO extends CacheInterface {

    @Set
    void set(String key, User user);

    @Set
    User set1(String key, User user);

    @Set
    void set2(String key, Long value);

    @Set
    Long set3(String key, Long value);

    @Set
    void set4(String key, String value);

    @Set
    String set5(String key, String value);

    @Get
    User get(String key);

    @Get
    Long get1(String key);

    @Get
    String get2(String key);

    @GetSet(expire = 100)
    User getSet(String key, User user);

    @GetSet(expire = 100)
    Long getSet1(String key, Long value);

    @GetSet(expire = 100)
    String getSet2(String key, String value);

    @Expire
    void expire(String key, int second);

    @Expire
    Long expire1(String key, int second);

    @Persist
    void persist(String key);

    @Ttl
    Long ttl(String key);

    @Incr(increment = 1)
    Long incr(String key);

    @Decr(decrement = 1)
    Long decr(String key);

    @Keys
    java.util.Set<String> keys(String pattern);

    @Type
    Class type(String key);
    CachedMap get();
}
