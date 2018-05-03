package com.rnkrsoft.framework.test.datasource;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.devops4j.message.MessageFormatter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 开发数据源
 */
@Slf4j
public class DevDataSource extends AbstractDataSource implements InitializingBean {
    @Setter
    String host = "localhost";
    @Setter
    int port = 3306;
    @Setter
    String schema;
    @Setter
    String username;
    @Setter
    String password;
    @Setter
    int initialSize = 5;
    @Setter
    int minIdle = 1;
    @Setter
    int maxActive = 5;
    @Setter
    int timeBetweenEvictionRunsSec = 1;
    @Setter
    int minEvictableIdleTimeSec = 2;


    BasicDataSource mysqlDataSource;
    BasicDataSource h2DataSource;

    @Override
    public Connection getConnection() throws SQLException {
        String dsn = DataSourceLookup.lookup();
        if (DataSourceLookup.H2_DATASOURCE.equals(dsn)) {
            return h2DataSource.getConnection();
        } else {
            return mysqlDataSource.getConnection();
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        String dsn = DataSourceLookup.lookup();
        if (DataSourceLookup.H2_DATASOURCE.equals(dsn)) {
            return h2DataSource.getConnection(username, password);
        } else {
            return mysqlDataSource.getConnection(username, password);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //=============================================设置H2数据源===========================================================
        this.h2DataSource = new BasicDataSource();
        this.h2DataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        this.h2DataSource.setUrl("jdbc:log4jdbc:h2:mem:test;MODE=MYSQL");
        this.h2DataSource.setUsername("sa");
        this.h2DataSource.setPassword("");
        this.h2DataSource.setInitialSize(10);
        this.h2DataSource.setMinIdle(10);
        this.h2DataSource.setMaxActive(10);
        this.h2DataSource.setTimeBetweenEvictionRunsMillis(10000);
        this.h2DataSource.setMinEvictableIdleTimeMillis(10000);
        this.h2DataSource.setValidationQuery("SELECT 'x'");
        this.h2DataSource.setTestWhileIdle(false);
        this.h2DataSource.setTestOnBorrow(false);
        this.h2DataSource.setTestOnReturn(false);
        //=============================================设置MySQL数据源===========================================================

        if(username == null){
            throw ErrorContextFactory.instance().message("MySQL 数据源未设置密码").runtimeException();
        }
        if(password == null){
            throw ErrorContextFactory.instance().message("MySQL 数据源未设置密码").runtimeException();
        }
        if(schema == null){
            throw ErrorContextFactory.instance().message("MySQL 数据源未设置数据库模式").runtimeException();
        }
        this.mysqlDataSource = new BasicDataSource();
        this.mysqlDataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        String url = MessageFormatter.format("jdbc:log4jdbc:mysql://{}:{}/{}?useUnicode=true", host, port, schema);
        log.debug("MySQL url: {}", url);
        this.mysqlDataSource.setUrl(url);
        this.mysqlDataSource.setUsername(username);
        this.mysqlDataSource.setPassword(password);
        this.mysqlDataSource.setInitialSize(initialSize);
        this.mysqlDataSource.setMinIdle(minIdle);
        this.mysqlDataSource.setMaxActive(maxActive);
        this.mysqlDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsSec * 1000);
        this.mysqlDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeSec * 1000);
        this.mysqlDataSource.setValidationQuery("SELECT 'x'");
        this.mysqlDataSource.setTestWhileIdle(true);
        this.mysqlDataSource.setTestOnBorrow(true);
        this.mysqlDataSource.setTestOnReturn(false);

    }
}
