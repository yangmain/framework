package com.rnkrsoft.framework.orm.mongo;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.Test;

import javax.management.Query;
import java.util.Arrays;
import java.util.Iterator;

import static com.mongodb.client.model.Filters.*;


/**
 * Created by rnkrsoft.com on 2018/6/10.
 */
public class TestMongo {
    @Test
    public void test1() {
//        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.0.111", 3017), MongoCredential.createScramSha1Credential("userName", "" , "xsxxxx".toCharArray()), MongoClientOptions.builder().build());
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
        FindIterable fi = table.find(Filters.all("xxx"));
        System.out.println();
    }

    @Test
    public void test2() {
        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
        //插入单个文件
        table.insertOne(new Document("_id", "测试11").append("name", "张三").append("age", 12));
        //插入多个文件
        table.insertMany(Arrays.asList(
                        new Document("_id", "测试211").append("name", "张三").append("age", 12),
                        new Document("_id", "测试31").append("name", "张三").append("age", 12))
        );
    }


    @Test
    public void test3() {
        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
        //更新符合条件的第一条
//        table.updateOne(eq("age", 12), new Document("$set", new Document("title", "测试123").append("name", "里斯")));
//        table.updateOne(Filters.eq("age", 12), new Document("$set", new Document("title", "测试123").append("name", "里斯")));
        table.updateMany(new Document("age", 12), new Document("$set", new Document("title", "测试").append("name", "里斯")));
    }

    @Test
    public void test4() {
        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
        table.deleteMany(new Document("age", 12).append("titile", "测试"));
    }

    @Test
    public void test5() {
        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
//        FindIterable fi = table.find(and(gte("age", 12)));
        FindIterable fi = table.find(new Document("age", new Document("$gte", 12)));
        Iterator<Document> it = fi.iterator();
        while (it.hasNext()){
//            System.out.println(it.next());
            Document document = it.next();
            System.out.println(document);
        }
    }

    @Test
    public void test6() {
        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
        long i1 = table.count(gte("age", 12));
        System.out.println(i1);
        long i2 = table.count(new Document("age", new Document("$gte", 12)));
        System.out.println(i2);
    }

    @Test
    public void test7() {
        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
        long i2 = table.count(new Document("age", new Document("$gte", 12)));
        System.out.println(i2);
    }


    @Test
    public void test8() {
        MongoClient mongoClient = new MongoClient("192.168.0.111", 3017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("xxxx");
        MongoCollection table = mongoDatabase.getCollection("table1");
        table.updateMany(eq("age", 12), new Document("$set",new Document("age", 123).append("title", "xxxxxxxxxxxxxxxxx")));
    }
}
