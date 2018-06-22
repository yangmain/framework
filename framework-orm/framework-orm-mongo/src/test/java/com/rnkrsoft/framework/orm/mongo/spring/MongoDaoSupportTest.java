package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.test.SpringTest;
import lombok.Data;
import lombok.ToString;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by liucheng on 2018/6/5.
 */
@ContextConfiguration("classpath*:testContext-mongo.xml")
public class MongoDaoSupportTest extends SpringTest {

    @Data
    @ToString
    @MongoTable(name = "table1")
    static class Bean {
        @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
        @MongoColumn(name = "_id")
        String _id;

        @MongoColumn(name = "name")
        String name1;

        @MongoColumn(name = "age")
        Integer age2;

        @MongoColumn(name = "title")
        String title1;

        public Bean() {
        }

        public Bean(String id, String name, Integer age) {
            this._id = id;
            this.name1 = name;
            this.age2 = age;
        }
    }

    @Test
    public void testInsert() throws Exception {
        Bean bean = new Bean(null, "张三", null);
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoClientOptions.builder().build());
        MongoDaoSupport mongoDaoSupport = new MongoDaoSupport(mongoClient.getDatabase("xxxx"), Bean.class) {};
        long count = mongoDaoSupport.count(bean);
        System.out.println(count);
        List<Bean> list = mongoDaoSupport.select(bean);
        System.out.println(list);

        mongoDaoSupport.insert(false, new Bean(null, "张三", 12213));

    }
}