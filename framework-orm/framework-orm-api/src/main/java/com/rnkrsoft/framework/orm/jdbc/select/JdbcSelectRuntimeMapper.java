package com.rnkrsoft.framework.orm.jdbc.select;

import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface JdbcSelectRuntimeMapper<Entity, Key> extends JdbcInterface {
    /**
     * 按照运行时配置的值条件进行匹配查找记录
     * @param entity 实体条件
     * @return 记录列表
     */
    List<Entity> selectRuntime(Key entity);
}
