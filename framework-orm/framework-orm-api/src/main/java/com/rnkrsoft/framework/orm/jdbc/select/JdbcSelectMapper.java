package com.rnkrsoft.framework.orm.jdbc.select;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcSelectMapper<Entity, PrimaryKey> extends
        JdbcSelectAllMapper<Entity, PrimaryKey>,
        JdbcSelectAndMapper<Entity, PrimaryKey>,
        JdbcSelectOrMapper<Entity, PrimaryKey>,
        JdbcSelectRuntimeMapper<Entity, PrimaryKey>,
        JdbcSelectByPrimaryKeyMapper<Entity, PrimaryKey>,
        JdbcSelectPageOrMapper<Entity, PrimaryKey>,
        JdbcSelectPageAndMapper<Entity, PrimaryKey>
{
}
