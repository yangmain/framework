package com.rnkrsoft.framework.test.datasource;

import com.rnkrsoft.framework.test.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/4/20.
 */
@Slf4j
public class DataSourceScannerTest {

    @Test
    public void testLookup() throws Exception {
        DataSourceScanner.setDateSourceName(DataSourceType.MySQL.name());
        String name = DataSourceScanner.lookup();
        System.out.println(name);
    }
}