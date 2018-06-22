package com.rnkrsoft.framework.orm.jdbc.delete;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcDeleteByPrimaryKeyMapper<Entity, PrimaryKey> extends JdbcInterface {
    /**
     * 根据物理主键进行删除
     *
     * @param pk
     * @return
     */
    int deleteByPrimaryKey(PrimaryKey pk);
}
