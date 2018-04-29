package com.rnkrsoft.framework.test;

import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.test.dao.OrmDemoDAO;
import com.rnkrsoft.framework.test.entity.OrmEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by rnkrsoft.com on 2018/4/20.
 */
@ContextConfiguration("classpath*:testContext-orm.xml")
@DataSource(DataSourceType.H2)
public class DataSourceTestTest extends DataSourceTest{
    @CreateTable(entities = OrmEntity.class,
            keywordMode = WordMode.lowerCase,
            sqlMode = WordMode.lowerCase,
            prefixMode = NameMode.entity,
            prefix = "xxxx",
            suffixMode = NameMode.createTest,
            suffix = "yyyy",
            schemaMode = NameMode.auto,
            schema = "xxxxxx",
            testBeforeDrop = false
    )
    @Test
    public void test1(){
        OrmDemoDAO ormDemoDAO = getBean(OrmDemoDAO.class);
        OrmEntity entity = new OrmEntity();
        entity.setAge(2);
        ormDemoDAO.insert(entity);
        System.out.println(entity);
//        ormDemoDAO.selectAll();
//        ormDemoDAO.selectAnd(entity);
//        ormDemoDAO.selectOr(entity);
//        entity.setAge(3);
//        ormDemoDAO.selectAnd(entity);
//        ormDemoDAO.selectOr(entity);
//        ormDemoDAO.updateByPrimaryKeySelective(entity);
//        ormDemoDAO.selectAll();
//        Assert.assertEquals(1, ormDemoDAO.deleteByPrimaryKey(entity.getSerialNo()));
//        Assert.assertEquals(0, ormDemoDAO.countAll());

    }
}