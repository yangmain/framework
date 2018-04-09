package com.rnkrsoft.framework.orm.count;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface CountOrMapper<T,K> {
    int countOr(T entity);
}
