package com.rnkrsoft.framework.orm.mongo.select;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.mongo.MongoInterface;

import java.util.List;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface MongoSelectMapper<Entity> extends MongoInterface {
    /**
     * 按照实体查询
     * @param entity 实体
     * @return 实体列表
     */
    List<Entity> select(Entity entity);

    /**
     * 按照键值对查询
     * @param parameters 键值对
     * @return 实体列表
     */
    List<Entity> select(Map<String, Object> parameters);

    /**
     * 分页查询
     * @param pagination 分页对象
     * @return 分页对象
     */
    Pagination<Entity> selectPage(Pagination<Entity> pagination);
}
