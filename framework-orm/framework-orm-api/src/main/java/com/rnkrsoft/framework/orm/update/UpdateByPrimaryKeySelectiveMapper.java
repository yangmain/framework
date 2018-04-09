package com.rnkrsoft.framework.orm.update;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface UpdateByPrimaryKeySelectiveMapper<T,K> {
    /**
     * 按照物理主键进行非null字段的更新
     * @param entity
     * @return
     */
    int updateByPrimaryKeySelective(T entity);
}
