package com.rnkrsoft.framework.orm.delete;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface TruncateMapper<T,K> {
    /**
     * 清空表
     * @return
     */
    int truncate();
}
