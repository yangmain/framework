package com.rnkrsoft.framework.orm.jdbc.lock;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface JdbcLockByForUpdateAndMapper<Entity, PrimaryKey> extends JdbcInterface {
    /**
     * 按照实体取值锁定记录
     *
     * @param entity 实体条件，只能支持=这种查询
     * @return 记录列表
     */
    List<Entity> lockByForUpdateAnd(Entity entity);
}
