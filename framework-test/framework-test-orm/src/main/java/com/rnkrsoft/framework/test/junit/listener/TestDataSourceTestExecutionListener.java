package com.rnkrsoft.framework.test.junit.listener;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.test.DataSource;
import com.rnkrsoft.framework.test.datasource.DataSourceLookup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 * 测试数据源监听器
 */
@Slf4j
public class TestDataSourceTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);
        final Class<?> testClass = testContext.getTestClass();
        DataSource ds = testClass.getAnnotation(DataSource.class);
        if(ds != null){
            String dsn = ds.value().name();
            log.debug("开发数据源 '{}'", dsn);
            String name = DataSourceLookup.setDateSourceName(dsn);
            if(!dsn.equals(name)){
                throw ErrorContextFactory.instance().message("DataSource set failure!").runtimeException();
            }
        }
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        super.afterTestClass(testContext);
        DataSourceLookup.setDateSourceName(null);
    }


    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 2;
    }
}
