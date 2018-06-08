package com.rnkrsoft.framework.orm.mongo.select;

import com.rnkrsoft.framework.orm.Pagination;

import java.util.List;
import java.util.Map;

/**
 * Created by woate on 2018/6/3.
 */
public interface SelectMongoMapper<Entity> {
    List<Entity> select(Entity entity);
    List<Entity> selectRuntime(Entity entity);
    List<Entity> select(Map<String, Object> parameters);
    Pagination<Entity> selectPage(Pagination<Entity> pagination);
}
