package com.rnkrsoft.framework.orm.jdbc.lock;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcLockByForUpdateByPrimaryKeyMapper<T,K> extends JdbcInterface {
    /**
     * 通过物理主键锁定记录
     * @param pk
     * @return
     */
    T lockByForUpdateByPrimaryKey(K pk);
}
