package com.rnkrsoft.framework.orm.mongo.delete;

import com.rnkrsoft.framework.orm.mongo.MongoInterface;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface MongoDeleteMapper<Entity> extends MongoInterface {
    /**
     * 根据实体中的非null字段作为条件进行删除
     * @param entity 实体
     * @return 影响条数
     */
    long delete(Entity entity);

    /**
     * 根据Map中的条件进行删除
     * @param parameter 参数
     * @return 影响条数
     */
    long delete(Map<String, Object> parameter);
}
