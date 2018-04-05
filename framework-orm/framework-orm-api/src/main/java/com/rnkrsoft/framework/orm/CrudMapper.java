package com.rnkrsoft.framework.orm;

import com.rnkrsoft.framework.orm.delete.DeleteMapper;
import com.rnkrsoft.framework.orm.update.UpdateMapper;
import com.rnkrsoft.framework.orm.count.CountMapper;
import com.rnkrsoft.framework.orm.insert.InsertMapper;
import com.rnkrsoft.framework.orm.lock.LockMapper;
import com.rnkrsoft.framework.orm.select.SelectMapper;

/**
 * Created by rnkrsoft on 2016/12/18.
 */
public interface CrudMapper<Entity, PrimaryKey> extends
        CountMapper<Entity, PrimaryKey>,
        SelectMapper<Entity, PrimaryKey>,
        InsertMapper<Entity, PrimaryKey>,
        UpdateMapper<Entity, PrimaryKey>,
        DeleteMapper<Entity, PrimaryKey>,
        LockMapper<Entity, PrimaryKey>
{
}
