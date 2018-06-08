package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.Table;
import com.rnkrsoft.framework.test.SpringTest;
import lombok.Data;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;

/**
 * Created by liucheng on 2018/6/5.
 */
@ContextConfiguration("classpath*:testContext-mongo.xml")
public class MongoDaoSupportTest extends SpringTest {


    @Test
    public void testDeleteByPrimaryKey() throws Exception {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        Bean bean = new Bean();
        bean.id = "";
        new MongoDaoSupport(mongoClient.getDatabase("xxxx"), Bean.class){}.deleteByPrimaryKey(bean);
    }

    @Data
    @Table(name = "tb_bean_info")
    static class Bean{
        @PrimaryKey()
        String id;
    }
    @Test
    public void testInsert() throws Exception {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        new MongoDaoSupport(mongoClient.getDatabase("xxxx"), Bean.class){}.insert(new Bean());

    }

    @Test
    public void testSelect() throws Exception {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        Bean bean1 = new Bean();
//        bean1.id = "1234";
//        new MongoDaoSupport(mongoClient.getDatabase("xxxx"), Bean.class){}.select(bean1);
//        mongoClient.getDatabase("xxxx").getCollection("tb_bean_info").find(Filters)
    }
}