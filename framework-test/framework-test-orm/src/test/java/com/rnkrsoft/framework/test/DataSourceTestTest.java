package com.rnkrsoft.framework.test;

import com.rnkrsoft.framework.test.dao.OrmDemoDAO;
import com.rnkrsoft.framework.test.entity.OrmEntity;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/4/20.
 */
@ContextConfiguration("classpath*:testContext-orm.xml")
public class DataSourceTestTest extends DataSourceTest{
    @CreateTable(entities = OrmEntity.class)
    @Test
    public void test1(){
        OrmDemoDAO ormDemoDAO = getBean(OrmDemoDAO.class);
        OrmEntity entity = new OrmEntity();
        entity.setAge(2);
        ormDemoDAO.insert(entity);
        System.out.println(ormDemoDAO.deleteByPrimaryKey(entity.getSerialNo()));
    }
}