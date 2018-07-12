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
    List<Entity> select(Entity entity, LogicMode logicMode);
    List<Entity> select(Map<String, Object> parameters, LogicMode logicMode);
    Pagination<Entity> selectPage(Pagination<Entity> pagination, LogicMode logicMode);
}
