package com.rnkrsoft.framework.orm.spring;

import com.rnkrsoft.framework.orm.spring.dao.DemoDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners( {ServletTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath*:testContext-orm.xml"})
public class TestDemo{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DemoDAO demoDAO;
    @Test
    public void test1(){
        jdbcTemplate.execute("create table DEMO_INF(serialNo varchar(36), age int, PRIMARY  KEY(serialNo))");
        demoDAO.countAll();
    }
}
