package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.jdbc.NumberColumn;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.jdbc.StringColumn;
import com.rnkrsoft.framework.orm.jdbc.Table;
import com.rnkrsoft.framework.orm.mongo.MongoCollection;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.test.SpringTest;
import lombok.Data;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by liucheng on 2018/6/5.
 */
@ContextConfiguration("classpath*:testContext-mongo.xml")
public class MongoDaoSupportTest extends SpringTest {

    @Data
    @MongoCollection(name = "table1")
    static class Bean {
        @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
        @MongoColumn(name = "_id")
        String _id;

        @MongoColumn(name = "name")
        String name;

        @MongoColumn(name = "age")
        Integer age;

        public Bean(String id, String name, Integer age) {
            this._id = id;
            this.name = name;
            this.age = age;
        }
    }

    @Data
    static class Bean1 {
        @MongoColumn(name = "name")
        String name;

        @MongoColumn(name = "age")
        Integer age;
    }

    @Test
    public void testInsert() throws Exception {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
//        new MongoDaoSupport(mongoClient.getDatabase("xxxx"), Bean.class) {
//        }.insert(new Bean(null, "this is a test", 21));

    }
}