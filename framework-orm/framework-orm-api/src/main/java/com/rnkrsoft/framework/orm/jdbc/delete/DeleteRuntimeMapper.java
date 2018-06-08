package com.rnkrsoft.framework.orm.jdbc.delete;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface DeleteRuntimeMapper<Entity, PrimaryKey> extends JdbcInterface {
    /**
     * 根据实体中的非null字段作为条件进行删除
     * @param entity
     * @return
     */
    int deleteRuntime(Entity entity);

}
