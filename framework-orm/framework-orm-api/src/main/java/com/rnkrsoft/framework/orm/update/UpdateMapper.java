package com.rnkrsoft.framework.orm.update;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface UpdateMapper<Entity, PrimaryKey> extends
        UpdateByPrimaryKeyMapper<Entity, PrimaryKey>,
        UpdateByPrimaryKeySelectiveMapper<Entity, PrimaryKey>
{
}
