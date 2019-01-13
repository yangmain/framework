package com.rnkrsoft.framework.test.datasource;

import com.rnkrsoft.framework.test.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/4/20.
 */
@Slf4j
public class DataSourceLookupTest {

    @Test
    public void testLookup() throws Exception {
        DataSourceLookup.setDateSourceName(DataSourceType.MySQL.name());
        String name = DataSourceLookup.lookup();
        System.out.println(name);
    }
}