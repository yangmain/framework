package com.rnkrsoft.framework.orm.select;

import java.util.List;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface SelectAndMapper<T,K> {
    /**
     * 按照实体取值查找记录
     * @param entity 实体条件，只能支持=这种查询
     * @return 记录列表
     */
    List<T> selectAnd(T entity);
}
