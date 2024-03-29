package com.rnkrsoft.framework.orm.mongo.update;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.mongo.MongoInterface;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface MongoUpdateMapper<Entity> extends MongoInterface {
    /**
     * 按照物理主键进行全字段更新
     * @param entity
     * @return
     */
    long updateByPrimaryKey(Entity entity);
    /**
     * 按照物理主键进行非null字段的更新
     * @param entity
     * @return
     */
    long updateByPrimaryKeySelective(Entity entity);

    /**
     * 按照条件进行更新
     * @param condition
     * @param entity
     * @return
     */
    long update(Entity condition,  Entity entity);
    /**
     * 按照条件进行更新非null字段的更新
     * @param condition
     * @param entity
     * @return
     */
    long updateSelective(Entity condition,  Entity entity);
    /**
     * 按照条件进行更新
     * @param condition
     * @param entity
     * @return
     */
    long update(Map<String, Object> condition, Map<String, Object> entity);
}
