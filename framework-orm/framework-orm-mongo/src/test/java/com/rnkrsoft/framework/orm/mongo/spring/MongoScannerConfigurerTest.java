package com.rnkrsoft.framework.orm.mongo.spring;

import com.rnkrsoft.framework.orm.mongo.example.example1.dao.Example1DAO;
import com.rnkrsoft.framework.orm.mongo.example.example1.entity.Example1Entity;
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
    Example1DAO example1DAO;
    @Test
    public void testAfterPropertiesSet() throws Exception {
        example1DAO.delete(Example1Entity.builder().name("sss").build());
        example1DAO.insertSelective(Example1Entity.builder().name("sss").age(12).data("xxxxxxxxxxxxxx").build());
        example1DAO.updateSelective(Example1Entity.builder().name("sss").build(), Example1Entity.builder().name("xxx").build());
        List<Example1Entity> list = example1DAO.select(Example1Entity.builder().name("xxx").build());
        System.out.println(list);
    }
}