package com.rnkrsoft.framework.orm.jdbc.insert;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcInsertMapper<Entity, PrimaryKey> extends
        JdbcInsertAllMapper<Entity, PrimaryKey>,
        JdbcInsertSelectiveMapper<Entity, PrimaryKey> {
}
