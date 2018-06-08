package com.rnkrsoft.framework.orm.jdbc.select;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface SelectMapper<Entity, PrimaryKey> extends
        SelectAllMapper<Entity, PrimaryKey>,
        SelectAndMapper<Entity, PrimaryKey>,
        SelectOrMapper<Entity, PrimaryKey>,
        SelectRuntimeMapper<Entity, PrimaryKey>,
        SelectByPrimaryKeyMapper<Entity, PrimaryKey>,
        SelectPageOrMapper<Entity, PrimaryKey>,
        SelectPageAndMapper<Entity, PrimaryKey>
{
}
