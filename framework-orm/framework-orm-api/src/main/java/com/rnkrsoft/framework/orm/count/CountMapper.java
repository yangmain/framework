package com.rnkrsoft.framework.orm.count;

/**
 * Created by rnkrsoft on 2016/12/18.
 */
public interface CountMapper<Entity, PrimaryKey>extends
        CountAndMapper<Entity, PrimaryKey>,
        CountOrMapper<Entity, PrimaryKey>,
        CountAllMapper<Entity, PrimaryKey> {
}
