package com.rnkrsoft.framework.orm.count;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface CountOrMapper<Entity, PrimaryKey> {
    int countOr(Entity entity);
}
