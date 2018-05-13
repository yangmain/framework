package com.rnkrsoft.framework.sequence;

import javax.sql.DataSource;

/**
 * Created by rnkrsoft.com on 2018/5/13.
 */
public interface DataSourceAware {
    /**
     * 设置数据源
     * @param dataSource
     */
    void setDataSource(DataSource dataSource);
}
