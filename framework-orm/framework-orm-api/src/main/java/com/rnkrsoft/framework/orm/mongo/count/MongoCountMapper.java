package com.rnkrsoft.framework.orm.mongo.count;

import com.rnkrsoft.framework.orm.mongo.MongoInterface;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface MongoCountMapper<Entity> extends MongoInterface {
    /**
     * 根据实体中的非null字段作为条件进行统计
     * @param entity 实体
     * @return 总条数
     */
    int count(Entity entity);

    /**
     * 根据实体中的键值作为条件进行统计
     * @param parameter 键值对
     * @return 总条数
     */
    int count(Map<String, Object> parameter);
}
