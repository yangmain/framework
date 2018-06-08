package com.rnkrsoft.framework.orm.jdbc.select;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface SelectAndMapper<T,K> extends JdbcInterface {
    /**
     * 按照实体取值查找记录
     * @param entity 实体条件，只能支持=这种查询
     * @return 记录列表
     */
    List<T> selectAnd(T entity);
}
