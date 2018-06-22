package com.rnkrsoft.framework.orm.mongo.update;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface MongoUpdateMapper<Entity> {
    /**
     * 按照物理主键进行全字段更新
     * @param entity
     * @return
     */
    int updateByPrimaryKey(Entity entity);
    /**
     * 按照物理主键进行非null字段的更新
     * @param entity
     * @return
     */
    int updateByPrimaryKeySelective(Entity entity);

    /**
     * 按照条件进行更新
     * @param condition
     * @param entity
     * @return
     */
    int update(Entity condition, Entity entity);
    /**
     * 按照条件进行更新
     * @param condition
     * @param entity
     * @return
     */
    int update(Map<String, Object> condition, Map<String, Object> entity);
}
