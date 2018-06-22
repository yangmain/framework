package com.rnkrsoft.framework.orm.jdbc.update;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcUpdateMapper<Entity, PrimaryKey> extends
        JdbcUpdateByPrimaryKeyMapper<Entity, PrimaryKey>,
        JdbcUpdateByPrimaryKeySelectiveMapper<Entity, PrimaryKey>
{
}
