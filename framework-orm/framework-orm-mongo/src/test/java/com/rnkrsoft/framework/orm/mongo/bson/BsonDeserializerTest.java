package com.rnkrsoft.framework.orm.mongo.bson;

import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import com.rnkrsoft.framework.orm.mongo.entity.OperateLogEntity;
import lombok.ToString;
import org.bson.Document;
import org.junit.Test;

import javax.print.Doc;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/8/3.
 */
public class BsonDeserializerTest {
    @ToString
    @MongoTable(name = "TB_DEMO", schema = "test_database")
    static class Demo implements Serializable {
        @MongoColumn
        String name;
        @MongoColumn
        int age;
        @MongoColumn
        Demo1 address1;

        public Demo() {
        }

        public Demo(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
    @ToString
    static class Demo1 implements Serializable{
        @MongoColumn
        String address;
        @MongoColumn
        long count;
        @MongoColumn
        Demo2 demo2;

        public Demo1() {
        }

        public Demo1(String address, long count) {
            this.address = address;
            this.count = count;
        }
    }
    @ToString
    static class Demo2 implements Serializable{
        @MongoColumn
        String title;

        public Demo2() {
        }

        public Demo2(String title) {
            this.title = title;
        }
    }

    @Test
    public void testDescribe() throws Exception {
        Demo demo = new Demo("ssss", 5);
        demo.address1 = new Demo1(null, 123456L);
        demo.address1.demo2 = new Demo2("dsxsxs");
        BsonSerializer<Demo> serializer = new BsonSerializer(Demo.class, false);
        BsonDeserializer<Demo> deserializer = new BsonDeserializer(Demo.class, true);
        Document map = serializer.serialize(demo, false);
        System.out.println(map.toJson());
        Demo demo11 = deserializer.deserialize(map);
        System.out.println(demo11);
    }

    @Test
    public void testSerialize() throws Exception {
        BsonSerializer<OperateLogEntity> serializer = new BsonSerializer(OperateLogEntity.class, true);
        BsonDeserializer<OperateLogEntity> deserializer = new BsonDeserializer(OperateLogEntity.class, true);
        Document document = serializer.serialize(OperateLogEntity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").build(), false);
        Object object = deserializer.deserialize(document);
        System.out.println(object);
    }
}