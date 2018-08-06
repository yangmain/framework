package com.rnkrsoft.framework.orm.mongo.example.example1;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.rnkrsoft.framework.orm.mongo.bson.BsonSerializer;
import com.rnkrsoft.framework.orm.mongo.example.example1.entity.Example1Entity;
import org.bson.Document;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2018/8/3.
 */
public class BsonSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        BsonSerializer serializer = new BsonSerializer(Example1Entity.class, false);
        Document document = serializer.serialize(Example1Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(new Date()).build(), true);
        System.out.println(document.toJson());
    }


    @Test
    public void testSerialize2() throws Exception {
        BsonSerializer serializer = new BsonSerializer(Example1Entity.class, false);
        Document document = serializer.serialize(Example1Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(new Date()).build(), false);
        System.out.println(document.toJson());
    }
}