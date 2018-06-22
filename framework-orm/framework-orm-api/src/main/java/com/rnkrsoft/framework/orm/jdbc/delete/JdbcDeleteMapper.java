package com.rnkrsoft.framework.orm.jdbc.delete;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcDeleteMapper<Entity, PrimaryKey> extends
        JdbcDeleteAndMapper<Entity, PrimaryKey>,
        JdbcDeleteOrMapper<Entity, PrimaryKey>,
        JdbcDeleteRuntimeMapper<Entity, PrimaryKey>,
        JdbcDeleteByPrimaryKeyMapper<Entity, PrimaryKey> {
}
