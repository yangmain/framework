package com.rnkrsoft.framework.orm.jdbc.count;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface CountOrMapper<Entity, PrimaryKey> extends JdbcInterface {
    int countOr(Entity entity);
}
