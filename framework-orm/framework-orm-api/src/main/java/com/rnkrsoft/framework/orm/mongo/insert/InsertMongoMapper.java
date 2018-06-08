package com.rnkrsoft.framework.orm.mongo.insert;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface InsertMongoMapper<Entity> {
    /**
     * 按照输入实体的每一个字段插入，如果为null则插入NULL
     * @param entity
     * @return
     */
    int insert(Entity entity);
    /**
     * 按照输入实体的每一个字段插入，如果为null则不插入
     * @param entity
     * @return
     */
    int insertSelective(Entity entity);
    int insert(Map<String, Object> parameter);
}
