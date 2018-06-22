package com.rnkrsoft.framework.orm.jdbc.count;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcCountMapper<Entity, PrimaryKey>extends
        JdbcCountAndMapper<Entity, PrimaryKey>,
        JdbcCountOrMapper<Entity, PrimaryKey>,
        JdbcCountAllMapper<Entity, PrimaryKey> {
}
