package com.rnkrsoft.framework.orm.select;

/**
 * Created by rnkrsoft on 2016/12/18.
 */
public interface SelectMapper<Entity, PrimaryKey> extends
        SelectAllMapper<Entity, PrimaryKey>,
        SelectAndMapper<Entity, PrimaryKey>,
        SelectOrMapper<Entity, PrimaryKey>,
        SelectByPrimaryKeyMapper<Entity, PrimaryKey>,
        SelectPageOrMapper<Entity, PrimaryKey>
{
}
