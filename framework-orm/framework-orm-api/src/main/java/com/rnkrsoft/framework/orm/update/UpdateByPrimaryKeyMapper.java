package com.rnkrsoft.framework.orm.update;

/**
 * Created by rnkrsoft on 2016/12/18.
 */
public interface UpdateByPrimaryKeyMapper<T,K> {
    /**
     * 按照物理主键进行整个字段更新
     * @param entity
     * @return
     */
    int updateByPrimaryKey(T entity);
}
