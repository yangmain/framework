package com.rnkrsoft.framework.test.datasource;

import com.rnkrsoft.framework.test.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 */
@Slf4j
public class DataSourceLookup {
    static ThreadLocal<String> currentDataSource = new ThreadLocal();
    public static final String H2_DATASOURCE = "h2DataSource";
    public static final String MYSQL_DATASOURCE = "mysqlDataSource";

    public static String setDateSourceName(String name) {
        currentDataSource.set(name);
        return currentDataSource.get();
    }

    /**
     * 获取数据源
     * @return 数据源名称
     */
    public static String lookup(){
        EnvironmentUtils.Environment env = EnvironmentUtils.determineRuntime();
        if(env == EnvironmentUtils.Environment.IDE_JUNIT){
            String dsn = currentDataSource.get();
            if(dsn == null || dsn.isEmpty()){
                log.info("unit test use :{}!", H2_DATASOURCE);
                return H2_DATASOURCE;
            }
            dsn = dsn.toLowerCase() + "DataSource";
            //其他情况根据选择项使用
            if (MYSQL_DATASOURCE.equals(dsn)
                    || H2_DATASOURCE.equals(dsn)) {
                log.info("unit test use :{}!", currentDataSource.get());
                return dsn;
            }else {//如果运行在IDE环境下，直接使用H2数据库
                    log.info("unit test use :{}!", H2_DATASOURCE);
                    return H2_DATASOURCE;
            }
        }else{
            log.info("junit test env :{}!", H2_DATASOURCE);
            return H2_DATASOURCE;
        }
    }
}
