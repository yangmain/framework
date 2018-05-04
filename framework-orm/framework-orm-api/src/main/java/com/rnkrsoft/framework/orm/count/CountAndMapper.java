package com.rnkrsoft.framework.orm.count;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface CountAndMapper<Entity, PrimaryKey> {
    /**
     * 根据实体中的非null字段作为条件进行统计
     * @param entity
     * @return
     */
    int countAnd(Entity entity);
}
