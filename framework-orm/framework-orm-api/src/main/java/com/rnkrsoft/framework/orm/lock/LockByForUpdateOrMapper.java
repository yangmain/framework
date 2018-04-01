package com.rnkrsoft.framework.orm.lock;

import java.util.List;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface LockByForUpdateOrMapper<T,K> {
    /**
     * 按照实体取值锁定记录
     * @param entity 实体条件，只能支持=这种查询
     * @return 记录列表
     */
    List<T> lockByForUpdateOr(T entity);
}
