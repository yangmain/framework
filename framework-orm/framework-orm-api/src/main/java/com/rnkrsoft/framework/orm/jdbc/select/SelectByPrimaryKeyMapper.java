package com.rnkrsoft.framework.orm.jdbc.select;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface SelectByPrimaryKeyMapper<T,K> extends JdbcInterface {
    /**
     * 根据物理主键查询
     * @param pk
     * @return
     */
    T selectByPrimaryKey(K pk);
}
