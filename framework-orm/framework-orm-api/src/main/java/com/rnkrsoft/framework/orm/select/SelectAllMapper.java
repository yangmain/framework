package com.rnkrsoft.framework.orm.select;

import java.util.List;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface SelectAllMapper<T,K> {
    /**
     * 查询表所有记录
     * @return
     */
    List<T> selectAll();
}
