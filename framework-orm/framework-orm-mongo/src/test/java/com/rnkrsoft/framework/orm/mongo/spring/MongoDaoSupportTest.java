package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.rnkrsoft.framework.orm.mongo.dao.OperateLogEntity;
import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import com.rnkrsoft.framework.sequence.spring.ClasspathSequenceServiceConfigure;
import com.rnkrsoft.framework.test.SpringTest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/6/5.
 */
@ContextConfiguration("classpath*:testContext-mongo.xml")
public class MongoDaoSupportTest extends SpringTest {

    @Test
    public void testInsert() throws Exception {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        MongoDaoClient mongoDaoSupport = new MongoDaoClient(mongoClient.getDatabase("xxxx"), OperateLogEntity.class) {};
        mongoDaoSupport.setSequenceServiceConfigure(new ClasspathSequenceServiceConfigure());
       mongoDaoSupport.insert(false, OperateLogEntity.builder().name("sss").age(12).data("xxxxxxxxxxxxxx").build());
//       mongoDaoSupport.insert(false, OperateLogEntity.builder().id("xxxxxxxxxxxxx").name("sss").age(12).data("xxxxxxxxxxxxxx").build());
//        long count = mongoDaoSupport.count(OperateLogEntity.builder().age(12).build());
//        System.out.println(count);
//        mongoDaoSupport.delete(OperateLogEntity.builder().age(12).build());
        List<OperateLogEntity> list = mongoDaoSupport.select(OperateLogEntity.builder().age(12).build(), LogicMode.AND);
        for (OperateLogEntity entity : list) {
            System.out.println(entity);
        }
    }
}