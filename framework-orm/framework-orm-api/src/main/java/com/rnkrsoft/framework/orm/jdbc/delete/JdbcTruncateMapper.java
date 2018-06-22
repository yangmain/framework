package com.rnkrsoft.framework.orm.jdbc.delete;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcTruncateMapper<Entity, PrimaryKey> extends JdbcInterface {
    /**
     * 清空表
     * @return
     */
    int truncate();
}
