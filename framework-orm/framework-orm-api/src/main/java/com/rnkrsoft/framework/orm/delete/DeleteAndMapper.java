package com.rnkrsoft.framework.orm.delete;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface DeleteAndMapper<Entity, PrimaryKey> {
    /**
     * 根据实体中的非null字段作为条件进行删除
     * @param entity
     * @return
     */
    int deleteAnd(Entity entity);
}
