package com.rnkrsoft.framework.orm.insert;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface InsertAllMapper<Entity, PrimaryKey> {
    /**
     * 按照输入实体的每一个字段插入，如果为null则插入NULL
     * @param entity
     * @return
     */
    int insert(Entity entity);
}
