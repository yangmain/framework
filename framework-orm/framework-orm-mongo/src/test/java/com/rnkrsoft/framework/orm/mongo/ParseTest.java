package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.mongo.count.MongoCountMapper;
import com.rnkrsoft.framework.orm.mongo.dao.OperateLogDAO;
import com.rnkrsoft.framework.orm.mongo.insert.MongoInsertMapper;
import org.junit.Test;

/**
 * Created by woate on 2018/7/11.
 */
public class ParseTest {
    @Test
    public void test1(){
        Class entityClass = GenericsExtractor.extractEntityClass(OperateLogDAO.class, MongoInsertMapper.class, MongoCountMapper.class);
        System.out.println(entityClass);
    }
}
