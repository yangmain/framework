package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.mongo.count.MongoCountMapper;
import com.rnkrsoft.framework.orm.mongo.example.example1.dao.Example1DAO;
import com.rnkrsoft.framework.orm.mongo.insert.MongoInsertMapper;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/7/11.
 */
public class ParseTest {
    @Test
    public void test1(){
        Class entityClass = GenericsExtractor.extractEntityClass(Example1DAO.class, MongoInsertMapper.class, MongoCountMapper.class);
        System.out.println(entityClass);
    }
}
