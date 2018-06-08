package com.rnkrsoft.framework.orm.jdbc.insert;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface InsertSelectiveMapper<Entity, PrimaryKey> extends JdbcInterface {
    /**
     * 按照输入实体的每一个字段插入，如果为null则不插入
     *
     * @param entity
     * @return
     */
    int insertSelective(Entity entity);
}
