package com.rnkrsoft.framework.test.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DevDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DataSourceScanner.lookup();
        return dataSource;
    }
}
