package com.rnkrsoft.framework.orm.mongo.example.example3;

import com.rnkrsoft.framework.orm.mongo.bson.BsonSerializer;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Example3Entity;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Inner1Object;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Inner2Object;
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
        BsonSerializer serializer = new BsonSerializer(Example3Entity.class, true);
        Document document = serializer.serialize(Example3Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(new Date()).inner1Object(Inner1Object.builder()
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
                        .name("sdasd")
                        .time(13L)
                        .time1(1345L)
                        .money(123.23213)
                        .money1(31.563)
                        .createDate(new Date())
                        .build())
                .build())
                .build(), true);
        System.out.println(document.toJson());
    }


    @Test
    public void testSerialize2() throws Exception {
        BsonSerializer serializer = new BsonSerializer(Example3Entity.class, true);
        Document document = serializer.serialize(Example3Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(new Date()).inner1Object(Inner1Object.builder()
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
                        .name("sdasd")
                        .time(13L)
                        .time1(1345L)
                        .money(123.23213)
                        .money1(31.563)
                        .createDate(new Date())
                        .build())
                .build())
                .build(), false);
        System.out.println(document.toJson());
    }
}