package com.rnkrsoft.framework.orm.jdbc.insert;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface InsertMapper<Entity, PrimaryKey> extends
        InsertAllMapper<Entity, PrimaryKey>,
        InsertSelectiveMapper<Entity, PrimaryKey> {
}
