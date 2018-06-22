package com.rnkrsoft.framework.orm.jdbc.update;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcUpdateByPrimaryKeySelectiveMapper<T,K> extends JdbcInterface {
    /**
     * 按照物理主键进行非null字段的更新
     * @param entity
     * @return
     */
    int updateByPrimaryKeySelective(T entity);
}
