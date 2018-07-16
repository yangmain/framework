package com.rnkrsoft.framework.orm.mongo.spring;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.mongo.dao.OperateLogDAO;
import com.rnkrsoft.framework.orm.mongo.entity.OperateLogEntity;
import com.rnkrsoft.framework.test.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/7/9.
 */
@ContextConfiguration("classpath*:MongoScannerConfigurerTest.xml")
public class MongoScannerConfigurerTest extends SpringTest{

    @Autowired
    OperateLogDAO operateLogDAO;
    @Test
    public void testAfterPropertiesSet() throws Exception {
        operateLogDAO.delete(OperateLogEntity.builder().name("sss").build(), LogicMode.OR);
        operateLogDAO.insertSelective(OperateLogEntity.builder().name("sss").age(12).data("xxxxxxxxxxxxxx").build());
        operateLogDAO.update(OperateLogEntity.builder().name("sss").build(), LogicMode.AND, OperateLogEntity.builder().name("xxx").build());
        List<OperateLogEntity> list = operateLogDAO.select(OperateLogEntity.builder().name("xxx").build(), LogicMode.AND);
        System.out.println(list);
    }
}