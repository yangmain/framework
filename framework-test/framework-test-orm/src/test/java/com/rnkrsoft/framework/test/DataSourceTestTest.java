package com.rnkrsoft.framework.test;

import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.test.dao.OrmDemoDAO;
import com.rnkrsoft.framework.test.entity.OrmEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/4/20.
 */
@CreateTable(entities = OrmEntity.class,
        keywordMode = WordMode.lowerCase,
        sqlMode = WordMode.lowerCase,
        prefixMode = NameMode.entity,
        prefix = "xxxx",
        suffixMode = NameMode.entity,
        suffix = "yyyy",
        schemaMode = NameMode.entity,
        schema = "xxxxxx",
        createBeforeTest = true,
        testBeforeDrop = false
)
@ContextConfiguration("classpath*:testContext-orm.xml")
@DataSource(DataSourceType.AUTO)
public class DataSourceTestTest extends DataSourceTest{
    @Test
    public void test1(){
        OrmDemoDAO ormDemoDAO = getBean(OrmDemoDAO.class);
        OrmEntity entity = new OrmEntity();
        entity.setAge(2);
        entity.setUserName(UUID.randomUUID().toString());
        entity.setCreateDate(new Date());
        entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
        ormDemoDAO.insert(entity);

    }
}