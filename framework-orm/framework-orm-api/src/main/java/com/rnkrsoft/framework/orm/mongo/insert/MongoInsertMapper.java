package com.rnkrsoft.framework.orm.mongo.insert;

import com.rnkrsoft.framework.orm.mongo.MongoInterface;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface MongoInsertMapper<Entity> extends MongoInterface {
    /**
     * 按照输入实体的每一个字段插入，如果为null则插入NULL
     * @param entity 实体
     * @return 影响条数
     */
    int insert(Entity entity);
    /**
     * 按照输入实体的每一个字段插入，如果为null则不插入
     * @param entity 实体
     * @return 影响条数
     */
    int insertSelective(Entity entity);

    /**
     * 按照键值对插入记录
     * @param parameter 键值对
     * @return 影响条数
     */
    int insert(Map<String, Object> parameter);
}
