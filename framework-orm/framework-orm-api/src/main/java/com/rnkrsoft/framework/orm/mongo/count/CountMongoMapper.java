package com.rnkrsoft.framework.orm.mongo.count;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface CountMongoMapper<Entity> {
    /**
     * 根据实体中的非null字段作为条件进行统计
     * @param entity
     * @return
     */
    int count(Entity entity);
    int delete(Map<String, Object> parameter);
}
