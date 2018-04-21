package com.rnkrsoft.framework.test.junit.listener;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.orm.mybatis.spring.mapper.OrmMappedStatementRegister;
import com.rnkrsoft.framework.orm.spring.OrmScannerConfigurer;
import com.rnkrsoft.framework.orm.spring.OrmSessionFactoryBean;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import com.rnkrsoft.framework.test.CreateTable;
import com.rnkrsoft.framework.test.TableNameMode;
import com.rnkrsoft.framework.test.datasource.DataSourceScanner;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 * 自动创建表结构
 * 标注类上的@CreateTable将被继承到每个方法
 *
 *
 */
public class CreateTableExecutionListener extends AbstractTestExecutionListener {

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
        CreateTable classCreateTable = (CreateTable)testClass.getAnnotation(CreateTable.class);
        List<Class> entities = new ArrayList();
        if(classCreateTable != null){
            entities.addAll(Arrays.asList(classCreateTable.entities()));
        }
        //从方法上获取@CreateTable注解
        CreateTable methodCreateTable = testMethod.getAnnotation(CreateTable.class);
        if (methodCreateTable != null) {
            Class[] classes = methodCreateTable.entities();
            boolean test = methodCreateTable.createBeforeTest();
            boolean drop = methodCreateTable.testBeforeDrop();
            WordMode sqlMode = methodCreateTable.sqlMode();
            WordMode keywordMode = methodCreateTable.keywordMode();
            String schema = methodCreateTable.schema();
            String prefix = methodCreateTable.prefix();
            String suffix = methodCreateTable.suffix();
            TableNameMode schemaMode = methodCreateTable.schemaMode();
            TableNameMode prefixMode = methodCreateTable.prefixMode();
            TableNameMode suffixMode = methodCreateTable.suffixMode();
            if(schemaMode == TableNameMode.ENTITY){
                schema = null;
            }
            if(prefixMode == TableNameMode.ENTITY){
                prefix = null;
            }
            if(suffixMode == TableNameMode.ENTITY){
                suffix = null;
            }
            for (Class clazz : classes) {
                JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
                if (drop) {
                    String dropSql = SqlScriptUtils.generateDropTable(clazz, schemaMode, schema, prefixMode, prefix, suffixMode, suffix, sqlMode, keywordMode, drop);
                    jdbcTemplate.execute(dropSql);
                }
                String createSql = SqlScriptUtils.generateCreateTable(clazz, schemaMode, schema, prefixMode, prefix, suffixMode, suffix, null, sqlMode, keywordMode, test);
                jdbcTemplate.execute(createSql);
            }
            ApplicationContext ctx = testContext.getApplicationContext();
            String sqlSessionFactoryName = StringUtils.firstCharToLower(OrmSessionFactoryBean.class.getSimpleName());
            if(!ctx.containsBean(sqlSessionFactoryName)){
                throw ErrorContextFactory.instance().message("'{}' do not exist bean name '{}'", OrmSessionFactoryBean.class, sqlSessionFactoryName).runtimeException();
            }
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory)ctx.getBean(sqlSessionFactoryName);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            //overload mapper
            OrmMappedStatementRegister.rescan(configuration, sqlMode, keywordMode, schemaMode, schema, prefixMode,  prefix, suffixMode, suffix);
        }

    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        final Method testMethod = testContext.getTestMethod();
        final Class<?> testClass = testContext.getTestClass();
        String dsn = DataSourceScanner.lookup();
        CreateTable classCreateTable = testClass.getAnnotation(CreateTable.class);
        List<Class> entities = new ArrayList();
        if(classCreateTable != null){
            entities.addAll(Arrays.asList(classCreateTable.entities()));
        }
        CreateTable methodCreateTable = testMethod.getAnnotation(CreateTable.class);
        if (methodCreateTable != null) {
            Class[] classes = methodCreateTable.entities();
            boolean drop = methodCreateTable.testBeforeDrop();
            WordMode sqlMode = methodCreateTable.sqlMode();
            WordMode keywordMode = methodCreateTable.keywordMode();
            String schema = methodCreateTable.schema();
            String prefix = methodCreateTable.prefix();
            String suffix = methodCreateTable.suffix();
            TableNameMode schemaMode = methodCreateTable.schemaMode();
            TableNameMode prefixMode = methodCreateTable.prefixMode();
            TableNameMode suffixMode = methodCreateTable.suffixMode();
            if(schemaMode == TableNameMode.ENTITY){
                schema = null;
            }
            if(prefixMode == TableNameMode.ENTITY){
                prefix = null;
            }
            if(suffixMode == TableNameMode.ENTITY){
                suffix = null;
            }
            if (drop && DataSourceScanner.H2_DATASOURCE.equals(dsn)) {
                for (Class clazz : classes) {
                    JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
                    String createSql = SqlScriptUtils.generateCreateTable(clazz, schemaMode, schema, prefixMode, prefix, suffixMode, suffix, null, sqlMode, keywordMode, true);
                    jdbcTemplate.execute(createSql);
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }
}
