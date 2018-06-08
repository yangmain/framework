package com.rnkrsoft.framework.orm.jdbc.delete;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface DeleteMapper<Entity, PrimaryKey> extends
        DeleteAndMapper<Entity, PrimaryKey>,
        DeleteOrMapper<Entity, PrimaryKey>,
        DeleteRuntimeMapper<Entity, PrimaryKey>,
        DeleteByPrimaryKeyMapper<Entity, PrimaryKey> {
}
