package com.rnkrsoft.framework.orm.jdbc.count;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcCountAllMapper<Entity, PrimaryKey> extends JdbcInterface{
    /**
     * 统计表记录数
     * @return
     */
    int countAll();
}
