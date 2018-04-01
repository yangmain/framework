package com.rnkrsoft.framework.orm.insert;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface InsertSelectiveMapper<Entity, PrimaryKey> {
    /**
     * 按照输入实体的每一个字段插入，如果为null则不插入
     * @param entity
     * @return
     */
    int insertSelective(Entity entity);
}
