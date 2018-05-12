package com.rnkrsoft.framework.test;

import com.rnkrsoft.framework.test.junit.listener.CreateTableExecutionListener;
import com.rnkrsoft.framework.test.junit.listener.TestDataSourceTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

/**
 * Created by rnkrsoft.com on 2018/4/18.
 * 用于数据库测试的基类
 */
@TestExecutionListeners({
        CreateTableExecutionListener.class,
        TestDataSourceTestExecutionListener.class
})
public abstract class DataSourceTest extends SpringTest {
}