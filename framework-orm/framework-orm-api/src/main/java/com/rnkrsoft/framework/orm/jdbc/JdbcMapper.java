package com.rnkrsoft.framework.orm.jdbc;

import com.rnkrsoft.framework.orm.jdbc.delete.JdbcDeleteMapper;
import com.rnkrsoft.framework.orm.jdbc.update.JdbcUpdateMapper;
import com.rnkrsoft.framework.orm.jdbc.count.JdbcCountMapper;
import com.rnkrsoft.framework.orm.jdbc.insert.JdbcInsertMapper;
import com.rnkrsoft.framework.orm.jdbc.lock.JdbcLockMapper;
import com.rnkrsoft.framework.orm.jdbc.select.JdbcSelectMapper;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcMapper<Entity, PrimaryKey> extends
        JdbcCountMapper<Entity, PrimaryKey>,
        JdbcSelectMapper<Entity, PrimaryKey>,
        JdbcInsertMapper<Entity, PrimaryKey>,
        JdbcUpdateMapper<Entity, PrimaryKey>,
        JdbcDeleteMapper<Entity, PrimaryKey>,
        JdbcLockMapper<Entity, PrimaryKey>
{
}
