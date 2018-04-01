package com.rnkrsoft.framework.orm.count;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface CountAllMapper<T,K> {
    /**
     * 统计表记录数
     * @return
     */
    int countAll();
}
