package com.rnkrsoft.framework.orm.jdbc.update;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcUpdateByPrimaryKeyMapper<T,K> extends JdbcInterface {
    /**
     * 按照物理主键进行整个字段更新
     * @param entity
     * @return
     */
    int updateByPrimaryKey(T entity);
}
