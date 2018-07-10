package com.rnkrsoft.framework.orm.mongo.delete;

import com.rnkrsoft.framework.orm.mongo.MongoInterface;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface MongoDeleteMapper<Entity> extends MongoInterface {
    /**
     * 根据实体中的非null字段作为条件进行删除
     * @param entity
     * @return
     */
    int delete(Entity entity);
    int delete(Map<String, Object> parameter);
}
