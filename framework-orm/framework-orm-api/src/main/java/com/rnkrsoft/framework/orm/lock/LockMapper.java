package com.rnkrsoft.framework.orm.lock;


/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface LockMapper<T,K> extends
        LockByForUpdateByPrimaryKeyMapper<T,K>,
        LockByForUpdateOrMapper<T,K>,
        LockByUpdateSetPrimaryKeyMapper<T,K>
{
}
