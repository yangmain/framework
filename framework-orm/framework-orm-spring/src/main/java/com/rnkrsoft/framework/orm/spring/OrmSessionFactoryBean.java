package com.rnkrsoft.framework.orm.spring;

import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.DatabaseType;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.mybatis.plugins.OrderByInterceptor;
import com.rnkrsoft.framework.orm.mybatis.plugins.PaginationStage1Interceptor;
import com.rnkrsoft.framework.orm.mybatis.plugins.PaginationStage2Interceptor;
import com.rnkrsoft.framework.orm.mybatis.spring.transaction.SpringManagedTransactionFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;


/**
 * Created by rnkrsoft.com on 2018/4/2.
 * ORM会话工厂Bean
 */
@Slf4j
public class OrmSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
    public static final String CACHE_ENABLED = "cacheEnabled";
    public static final String LOCAL_CACHE_SCOPE = "localCacheScope";
    public static final String LAZY_LOADING_ENABLED = "lazyLoadingEnabled";
    public static final String MULTIPLE_RESULT_SETS_ENABLED = "multipleResultSetsEnabled";
    public static final String USE_COLUMN_LABEL = "useColumnLabel";
    public static final String DEFAULT_EXECUTOR_TYPE = "defaultExecutorType";
    public static final String DEFAULT_STATEMENT_TIMEOUT = "defaultStatementTimeout";
    /**
     * 数据源
     */
    @Autowired(required = false)
    @Qualifier("defaultDataSource")
    DataSource dataSource;

    @Setter
    Properties configurationProperties;//用动态配置对象替换

    SqlSessionFactory sqlSessionFactory;
    /**
     * 事务工厂
     */
    TransactionFactory transactionFactory;
    /**
     * MyBatis插件
     */
    @Setter
    Interceptor[] plugins;

    @Setter
    String environment = StringUtils.firstCharToLower(OrmSessionFactoryBean.class.getSimpleName());

    @Setter
    String beanName = StringUtils.firstCharToLower(OrmSessionFactoryBean.class.getSimpleName());

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            // If we got a TransactionAwareDataSourceProxy, we need to perform
            // transactions for its underlying target DataSource, else data
            // access code won't see properly exposed transactions (i.e.
            // transactions for the target DataSource).
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }
    }

    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }
        return this.sqlSessionFactory;
    }

    @Override
    public Class<? extends SqlSessionFactory> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initDefaultPlugins();
        initDefaultConfig();
        this.sqlSessionFactory = buildSqlSessionFactory();
    }

    void initDefaultConfig() {
        if (this.configurationProperties == null) {
            this.configurationProperties = new Properties();
        }
        if (!this.configurationProperties.containsKey(CACHE_ENABLED)) {
            //对在此配置文件下的所有cache 进行全局性开/关设置
            this.configurationProperties.setProperty(CACHE_ENABLED, "false");
        }
        if (!this.configurationProperties.containsKey(LOCAL_CACHE_SCOPE)) {
            //
            this.configurationProperties.setProperty(LOCAL_CACHE_SCOPE, "STATEMENT");
        }
        if (!this.configurationProperties.containsKey(LAZY_LOADING_ENABLED)) {
            //全局性设置懒加载。如果设为‘false’，则所有相关联的都会被初始化加载。
            this.configurationProperties.setProperty(LAZY_LOADING_ENABLED, "false");
        }
        if (!this.configurationProperties.containsKey(MULTIPLE_RESULT_SETS_ENABLED)) {
            //允许和不允许单条语句返回多个数据集（取决于驱动需求）
            this.configurationProperties.setProperty(MULTIPLE_RESULT_SETS_ENABLED, "true");
        }
        if (!this.configurationProperties.containsKey(USE_COLUMN_LABEL)) {
            //使用列标签代替列名称。不同的驱动器有不同的作法。参考一下驱动器文档，或者用这两个不同的选项进行测试一下。
            this.configurationProperties.setProperty(USE_COLUMN_LABEL, "true");
        }
        if (!this.configurationProperties.containsKey(DEFAULT_EXECUTOR_TYPE)) {
            //配置和设定执行器，SIMPLE 执行器执行其它语句。REUSE 执行器可能重复使用prepared statements 语句，BATCH执行器可以重复执行语句和批量更新。
            this.configurationProperties.setProperty(DEFAULT_EXECUTOR_TYPE, "REUSE");
        }
        if (!this.configurationProperties.containsKey(DEFAULT_STATEMENT_TIMEOUT)) {
            //设置一个时限，以决定让驱动器等待数据库回应的多长时间为超时
            this.configurationProperties.setProperty(DEFAULT_STATEMENT_TIMEOUT, "1000");
        }
    }

    SqlSessionFactory buildSqlSessionFactory() throws IOException {
        Configuration configuration = new Configuration();
        configuration.setVariables(this.configurationProperties);
        if (this.plugins != null && this.plugins.length > 0) {
            for (Interceptor plugin : this.plugins) {
                configuration.addInterceptor(plugin);
                if (log.isDebugEnabled()) {
                    log.debug("Registered plugin: '" + plugin + "'");
                }
            }
        }
        if (this.transactionFactory == null) {
            this.transactionFactory = new SpringManagedTransactionFactory();
        }
        configuration.setEnvironment(new Environment(this.environment, this.transactionFactory, this.dataSource));
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    /**
     * 初始化默认插件
     */
    void initDefaultPlugins() {
        //默认加入分页插件
        boolean paginationStage1Interceptor = false;
        boolean paginationStage2Interceptor = false;
        List<Interceptor> pluginSet = null;
        if (plugins != null) {
            for (Interceptor plugin : plugins) {
                if (plugin instanceof PaginationStage1Interceptor) {
                    paginationStage1Interceptor = true;
                }
                if (plugin instanceof PaginationStage2Interceptor) {
                    paginationStage2Interceptor = true;
                }
            }
            pluginSet = Arrays.asList(plugins);
        } else {
            pluginSet = new ArrayList();
        }


        if (!paginationStage1Interceptor) {
            pluginSet.add(new PaginationStage1Interceptor(DatabaseType.MySQL));
        }
        pluginSet.add(new OrderByInterceptor());
        if (!paginationStage2Interceptor) {
            pluginSet.add(new PaginationStage2Interceptor());
        }
        this.plugins = pluginSet.toArray(new Interceptor[pluginSet.size()]);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
    }}
