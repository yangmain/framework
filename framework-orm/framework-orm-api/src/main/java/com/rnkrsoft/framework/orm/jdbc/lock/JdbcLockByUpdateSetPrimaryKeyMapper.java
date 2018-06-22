package com.rnkrsoft.framework.orm.jdbc.lock;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcLockByUpdateSetPrimaryKeyMapper<T,K> extends JdbcInterface {
    /**
     * 通过更新主键锁定记录
     * @param pk
     * @return
     */
    int lockByUpdateSetPrimaryKey(K pk);
}
