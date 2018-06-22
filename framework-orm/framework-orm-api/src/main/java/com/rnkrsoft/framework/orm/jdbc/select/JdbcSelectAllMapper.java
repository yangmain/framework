package com.rnkrsoft.framework.orm.jdbc.select;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcSelectAllMapper<T,K> extends JdbcInterface {
    /**
     * 查询表所有记录
     * @return
     */
    List<T> selectAll();
}
