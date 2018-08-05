package com.rnkrsoft.framework.orm.mongo.client;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.rnkrsoft.framework.orm.mongo.entity.OperateLogEntity;
import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.sequence.spring.ClasspathSequenceServiceConfigure;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/5.
 */
public class MongoDaoSupportTest {

    @Test
    public void testInsert() throws Exception {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        MongoDaoClient mongoDaoSupport = new MongoDaoClient(mongoClient, "xxxx", OperateLogEntity.class) {
        };
        mongoDaoSupport.setSequenceServiceConfigure(new ClasspathSequenceServiceConfigure());
//        OperateLogEntity operateLogEntity = OperateLogEntity.builder().id(UUID.randomUUID().toString()).name("sss").age(12).data("xxxxxxxxxxxxxx").build();
//       mongoDaoSupport.insert(operateLogEntity);
//        System.out.println(operateLogEntity.getId());
        long updateCnt = mongoDaoSupport.updateSelective(OperateLogEntity.builder().id("78bc7585-4e3d-47ec-b483-e66b98fa7ec4").age(26).build(), OperateLogEntity.builder().name("zzzz").age(26).build());
        System.out.println(updateCnt);
//       mongoDaoSupport.insert(false, OperateLogEntity.builder().id("xxxxxxxxxxxxx").name("sss").age(12).data("xxxxxxxxxxxxxx").build());
//        long count = mongoDaoSupport.count(OperateLogEntity.builder().age(12).build());
//        System.out.println(count);
//        mongoDaoSupport.delete(OperateLogEntity.builder().age(12).build());
//        List<OperateLogEntity> list = mongoDaoSupport.select(OperateLogEntity.builder().age(12).build());
//        for (OperateLogEntity entity : list) {
//            System.out.println(entity);
//        }
    }
}