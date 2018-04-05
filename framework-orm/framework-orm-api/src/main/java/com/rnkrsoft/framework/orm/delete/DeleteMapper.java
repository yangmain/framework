package com.rnkrsoft.framework.orm.delete;

/**
 * Created by rnkrsoft on 2016/12/18.
 */
public interface DeleteMapper<Entity, PrimaryKey> extends
        DeleteAndMapper<Entity, PrimaryKey>,
        DeleteOrMapper<Entity, PrimaryKey>,
        DeleteByPrimaryKeyMapper<Entity, PrimaryKey> {
}
