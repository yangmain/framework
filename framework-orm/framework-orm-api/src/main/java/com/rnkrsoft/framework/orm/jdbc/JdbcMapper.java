package com.rnkrsoft.framework.orm.jdbc;

import com.rnkrsoft.framework.orm.jdbc.delete.DeleteMapper;
import com.rnkrsoft.framework.orm.jdbc.update.UpdateMapper;
import com.rnkrsoft.framework.orm.jdbc.count.CountMapper;
import com.rnkrsoft.framework.orm.jdbc.insert.InsertMapper;
import com.rnkrsoft.framework.orm.jdbc.lock.LockMapper;
import com.rnkrsoft.framework.orm.jdbc.select.SelectMapper;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcMapper<Entity, PrimaryKey> extends
        CountMapper<Entity, PrimaryKey>,
        SelectMapper<Entity, PrimaryKey>,
        InsertMapper<Entity, PrimaryKey>,
        UpdateMapper<Entity, PrimaryKey>,
        DeleteMapper<Entity, PrimaryKey>,
        LockMapper<Entity, PrimaryKey>
{
}
