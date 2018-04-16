package com.rnkrsoft.framework.orm.spring;

import com.rnkrsoft.framework.orm.spring.dao.DemoDAO;
import com.rnkrsoft.framework.orm.spring.entity.DemoEntity;
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
    DemoDAO demoDAO;
    @Test
    public void test1(){
        jdbcTemplate.execute("CREATE TABLE DEMO_INF(SERIAL_NO VARCHAR(36), AGE INT, PRIMARY  KEY(SERIAL_NO))");
        System.out.println(demoDAO.countAll());
        {
            DemoEntity entity = new DemoEntity();
//        entity.setSerial(UUID.randomUUID().toString());
            entity.setAge(12);
            demoDAO.insertSelective(entity);
        }
        {
            DemoEntity entity = new DemoEntity();
//        entity.setSerial(UUID.randomUUID().toString());
            entity.setAge(12);
            demoDAO.insertSelective(entity);
        }
        System.out.println(demoDAO.countAll());
    }
}
