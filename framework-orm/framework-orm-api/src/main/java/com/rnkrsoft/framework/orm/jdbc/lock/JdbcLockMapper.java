package com.rnkrsoft.framework.orm.jdbc.lock;


/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcLockMapper<T,K> extends
        JdbcLockByForUpdateByPrimaryKeyMapper<T,K>,
        JdbcLockByForUpdateOrMapper<T,K>,
        JdbcLockByUpdateSetPrimaryKeyMapper<T,K>
{
}
