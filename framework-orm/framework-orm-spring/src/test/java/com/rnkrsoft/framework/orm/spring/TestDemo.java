package com.rnkrsoft.framework.orm.spring;

import com.rnkrsoft.framework.orm.spring.dao.JpaDemoDAO;
import com.rnkrsoft.framework.orm.spring.dao.OrmDemoDAO;
import com.rnkrsoft.framework.orm.spring.entity.JpaEntity;
import com.rnkrsoft.framework.orm.spring.entity.OrmEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.UUID;

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
    JpaDemoDAO jpaDemoDAO;
    @Autowired
    OrmDemoDAO ormDemoDAO;
    @Test
    public void test1(){
        jdbcTemplate.execute("CREATE TABLE JPA_DEMO_INF(SERIAL_NO VARCHAR(36), AGE INT, PRIMARY  KEY(SERIAL_NO))");
        System.out.println(jpaDemoDAO.countAll());
        for (int i = 0; i < 1 ; i++) {
            JpaEntity entity = new JpaEntity();
            entity.setSerialNo(UUID.randomUUID().toString());
            entity.setAge(12);
            jpaDemoDAO.insertSelective(entity);
        }
        System.out.println(jpaDemoDAO.countAll());
    }

    @Test
    public void test2(){
        jdbcTemplate.execute("CREATE TABLE ORM_DEMO_INF(SERIAL_NO VARCHAR(36), AGE INT, PRIMARY  KEY(SERIAL_NO))");
        System.out.println(ormDemoDAO.countAll());
        for (int i = 0; i < 1000 ; i++) {
            OrmEntity entity = new OrmEntity();
            entity.setAge(12);
            ormDemoDAO.insertSelective(entity);
        }
        System.out.println(ormDemoDAO.countAll());
    }
}
