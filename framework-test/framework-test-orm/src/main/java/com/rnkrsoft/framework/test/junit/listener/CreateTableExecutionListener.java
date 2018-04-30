package com.rnkrsoft.framework.test.junit.listener;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.orm.config.ItemConfig;
import com.rnkrsoft.framework.orm.mybatis.sequence.SequenceServiceConfigure;
import com.rnkrsoft.framework.orm.mybatis.spring.mapper.OrmMappedStatementRegister;
import com.rnkrsoft.framework.orm.spring.OrmScannerConfigurer;
import com.rnkrsoft.framework.orm.spring.OrmSessionFactoryBean;
import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.test.config.TestOrmConfig;
import com.rnkrsoft.framework.test.junit.listener.createtable.CreateTableContext;
import com.rnkrsoft.framework.test.junit.listener.createtable.CreateTableHandler;
import com.rnkrsoft.framework.test.junit.listener.createtable.CreateTableScope;
import com.rnkrsoft.framework.test.junit.listener.createtable.CreateTableWrapper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 * 自动创建表结构
 * 标注类上的@CreateTable将被继承到每个方法
 */
public class CreateTableExecutionListener extends AbstractTestExecutionListener {

    CreateTableHandler createTableHandler = new CreateTableHandler();

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        final Class testClass = testContext.getTestClass();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        final Class testClass = testContext.getTestClass();

    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        final Class testClass = testContext.getTestClass();
        final Method testMethod = testContext.getTestMethod();
        ItemConfig globalConfig = new ItemConfig();
        WordMode sqlMode = WordMode.upperCase;
        WordMode keywordMode = WordMode.upperCase;
        NameMode schemaMode = NameMode.auto;
        String schema = null;
        NameMode prefixMode = NameMode.auto;
        String prefix = null;
        NameMode suffixMode = NameMode.auto;
        String suffix = null;
        CreateTable classCreateTable = (CreateTable) testClass.getAnnotation(CreateTable.class);
        if (classCreateTable != null) {
            Class[] classes = classCreateTable.entities();
            boolean createBeforeTest = classCreateTable.createBeforeTest();
            boolean testBeforeDrop = classCreateTable.testBeforeDrop();
            sqlMode = classCreateTable.sqlMode();
            keywordMode = classCreateTable.keywordMode();
            schemaMode = classCreateTable.schemaMode();
            schema = classCreateTable.schema();
            prefixMode = classCreateTable.prefixMode();
            prefix = classCreateTable.prefix();
            suffixMode = classCreateTable.suffixMode();
            suffix = classCreateTable.suffix();
            CreateTableContext context = CreateTableContext.context();
            for (Class entityClass : classes) {
                CreateTableWrapper createTableWrapper = CreateTableWrapper.builder()
                        .scope(CreateTableScope.Class)
                        .entityClass(entityClass)
                        .createBeforeTest(createBeforeTest)
                        .testBeforeDrop(testBeforeDrop)
                        .sqlMode(sqlMode)
                        .keywordMode(keywordMode)
                        .schemaMode(schemaMode)
                        .schema(schema)
                        .prefixMode(prefixMode)
                        .prefix(prefix)
                        .suffixMode(suffixMode)
                        .suffix(suffix)
                        .build();
                context.addTable(createTableWrapper);
            }

        }
        CreateTable methodCreateTable = testMethod.getAnnotation(CreateTable.class);
        if (methodCreateTable != null) {
            Class[] classes = methodCreateTable.entities();
            boolean createBeforeTest = methodCreateTable.createBeforeTest();
            boolean testBeforeDrop = methodCreateTable.testBeforeDrop();
            sqlMode = methodCreateTable.sqlMode();
            keywordMode = methodCreateTable.keywordMode();
            schemaMode = methodCreateTable.schemaMode();
            schema = methodCreateTable.schema();
            prefixMode = methodCreateTable.prefixMode();
            prefix = methodCreateTable.prefix();
            suffixMode = methodCreateTable.suffixMode();
            suffix = methodCreateTable.suffix();
            CreateTableContext context = CreateTableContext.context();
            List<Class> methodEntities_ = Arrays.asList(classes);
            //方法上的实体只有类上实体没包含才加入
            for (Class entityClass : classes) {
                CreateTableWrapper createTableWrapper = CreateTableWrapper.builder()
                        .scope(CreateTableScope.Method)
                        .entityClass(entityClass)
                        .createBeforeTest(createBeforeTest)
                        .testBeforeDrop(testBeforeDrop)
                        .sqlMode(sqlMode)
                        .keywordMode(keywordMode)
                        .schemaMode(schemaMode)
                        .schema(schema)
                        .prefixMode(prefixMode)
                        .prefix(prefix)
                        .suffixMode(suffixMode)
                        .suffix(suffix)
                        .build();
                context.addTable(createTableWrapper);
            }
        }
        ApplicationContext ctx = testContext.getApplicationContext();
        CreateTableContext context = CreateTableContext.context();
        DataSource dataSource = ctx.getBean(DataSource.class);
        Connection connection = dataSource.getConnection();
        createTableHandler.create(connection, context);
        connection.close();
        String sqlSessionFactoryName = StringUtils.firstCharToLower(OrmSessionFactoryBean.class.getSimpleName());
        if (!ctx.containsBean(sqlSessionFactoryName)) {
            throw ErrorContextFactory.instance().message("'{}' do not exist bean name '{}'", OrmSessionFactoryBean.class, sqlSessionFactoryName).runtimeException();
        }
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) ctx.getBean(sqlSessionFactoryName);

        String ormScannerConfigurerName = StringUtils.firstCharToLower(OrmScannerConfigurer.class.getSimpleName());
        if (!ctx.containsBean(ormScannerConfigurerName)) {
            throw ErrorContextFactory.instance().message("'{}' do not exist bean name '{}'", OrmScannerConfigurer.class, ormScannerConfigurerName).runtimeException();
        }
        OrmScannerConfigurer ormScannerConfigurer = (OrmScannerConfigurer) ctx.getBean(ormScannerConfigurerName);
        Configuration configuration = sqlSessionFactory.getConfiguration();

        String sequenceServiceConfigureName = StringUtils.firstCharToLower(SequenceServiceConfigure.class.getSimpleName());
        if (!ctx.containsBean(sequenceServiceConfigureName)) {
            throw ErrorContextFactory.instance().message("'{}' do not exist bean name '{}'", SequenceServiceConfigure.class, sequenceServiceConfigureName).runtimeException();
        }
        SequenceServiceConfigure sequenceServiceConfigure = (SequenceServiceConfigure) ctx.getBean(sequenceServiceConfigureName);
        TestOrmConfig ormConfig = (TestOrmConfig) ormScannerConfigurer.getOrmConfig();
        ormConfig.setKeywordMode(keywordMode);
        ormConfig.setSqlMode(sqlMode);

        globalConfig.setSchemaMode(context.getSchemaMode());
        globalConfig.setSchema(context.getSchema());
        globalConfig.setPrefixMode(context.getPrefixMode());
        globalConfig.setPrefix(context.getPrefix());
        globalConfig.setSuffixMode(context.getSuffixMode());
        globalConfig.setSuffix(context.getSuffix());
        ormConfig.setGlobal(globalConfig);
        //overload mapper
        OrmMappedStatementRegister.rescan(configuration, ormConfig, sequenceServiceConfigure);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        final Method testMethod = testContext.getTestMethod();
        final Class<?> testClass = testContext.getTestClass();
        ApplicationContext ctx = testContext.getApplicationContext();
        DataSource dataSource = ctx.getBean(DataSource.class);
        Connection connection = dataSource.getConnection();
        CreateTableContext context = CreateTableContext.context();
        createTableHandler.drop(connection, context);
        connection.close();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }
}
