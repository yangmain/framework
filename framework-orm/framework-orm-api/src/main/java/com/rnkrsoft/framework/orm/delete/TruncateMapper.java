package com.rnkrsoft.framework.orm.delete;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface TruncateMapper<Entity, PrimaryKey> {
    /**
     * 清空表
     * @return
     */
    int truncate();
}
