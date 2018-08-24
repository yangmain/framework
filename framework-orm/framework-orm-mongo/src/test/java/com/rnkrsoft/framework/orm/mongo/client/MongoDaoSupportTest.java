package com.rnkrsoft.framework.orm.mongo.client;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.rnkrsoft.framework.orm.mongo.example.example1.entity.Example1Entity;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Example3Entity;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Inner1Object;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Inner2Object;
import com.rnkrsoft.framework.sequence.spring.ClasspathSequenceServiceConfigure;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/5.
 */
public class MongoDaoSupportTest {

    @Test
    public void testInsert() throws Exception {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        MongoDaoClient mongoDaoSupport = new MongoDaoClient(mongoClient, "xxxx", Example1Entity.class) {
        };
        mongoDaoSupport.setSequenceServiceConfigure(new ClasspathSequenceServiceConfigure());
        Example1Entity example1Entity = Example1Entity.builder().id(UUID.randomUUID().toString()).name("sss").age(12).data("xxxxxxxxxxxxxx").createDate(new Date()).build();
        mongoDaoSupport.insert(example1Entity);
//        System.out.println(example1Entity.getId());
        long updateCnt = mongoDaoSupport.updateSelective(Example1Entity.builder().id("78bc7585-4e3d-47ec-b483-e66b98fa7ec4").age(26).build(), Example1Entity.builder().name("zzzz").age(26).build());
        System.out.println(updateCnt);
//       mongoDaoSupport.insert(false, Example3Entity.builder().id("xxxxxxxxxxxxx").name("sss").age(12).data("xxxxxxxxxxxxxx").build());
//        long count = mongoDaoSupport.count(Example3Entity.builder().age(12).build());
//        System.out.println(count);
//        mongoDaoSupport.delete(Example3Entity.builder().age(12).build());
//        List<Example3Entity> list = mongoDaoSupport.select(Example3Entity.builder().age(12).build());
//        for (Example3Entity entity : list) {
//            System.out.println(entity);
//        }
    }

    @Test
    public void testInsert2() {
        Example3Entity entity = Example3Entity.builder()
                .age(12)
                .data("sssss")
                .id(UUID.randomUUID().toString())
                .name("mike")
                .amt(new BigDecimal("123456789.12356468951"))
                .createDate(new Date())
                .inner1Object(Inner1Object.builder()
                        .age(1)
                        .age1(2)
                        .name("xsfda")
                        .time(123L)
                        .time1(12345L)
                        .money(123.23213)
                        .money1(31.563)
                        .createDate(new Date())
                        .inner2Object(Inner2Object.builder()
                                .age(2)
                                .age1(3)
//                        .name("sdasd")
                                .time(13L)
                                .time1(1345L)
                                .money(123.23213)
                                .money1(31.563)
                                .createDate(new Date())
                                .build())
                        .build())
                .inner1Object2(Inner1Object.builder()
                        .age(23)
                        .age1(231)
                        .name("112312")
                        .time(123L)
                        .time1(12345L)
                        .money(123.23213)
                        .money1(31.563)
                        .createDate(new Date())
                        .inner2Object(Inner2Object.builder()
                                .age(2)
                                .age1(3)
//                        .name("sdasd")
                                .time(13L)
                                .time1(1345L)
                                .money(123.23213)
                                .money1(31.563)
                                .createDate(new Date())
                                .build())
                        .build())
                .build();
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        MongoDaoClient mongoDaoSupport = new MongoDaoClient(mongoClient, "xxxx", Example3Entity.class) {
        };
        mongoDaoSupport.setSequenceServiceConfigure(new ClasspathSequenceServiceConfigure());
        mongoDaoSupport.insert(entity);
    }

    @Test
    public void testSelect2() {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        MongoDaoClient<Example3Entity> mongoDaoSupport = new MongoDaoClient(mongoClient, "xxxx", Example3Entity.class) {
        };
        for (int i = 0; i < 100; i++) {

            Example3Entity entity = mongoDaoSupport.selectByPrimaryKey("2c780dba-7028-4f27-aa30-8b4397430506");
            System.out.println(entity);
        }
    }
}