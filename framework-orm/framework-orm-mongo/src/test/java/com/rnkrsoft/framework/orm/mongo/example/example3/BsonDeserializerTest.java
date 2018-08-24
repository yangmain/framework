package com.rnkrsoft.framework.orm.mongo.example.example3;

import com.rnkrsoft.framework.orm.mongo.bson.BsonDeserializer;
import com.rnkrsoft.framework.orm.mongo.bson.BsonSerializer;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Example3Entity;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Inner1Object;
import com.rnkrsoft.framework.orm.mongo.example.example3.entity.Inner2Object;
import com.rnkrsoft.utils.DateUtils;
import org.bson.Document;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2018/8/3.
 */
public class BsonDeserializerTest {
    @Test
    public void testSerialize() throws Exception {
        BsonSerializer<Example3Entity> serializer = new BsonSerializer(Example3Entity.class, true);
        BsonDeserializer<Example3Entity> deserializer = new BsonDeserializer(Example3Entity.class, true);
        Document document = serializer.serialize(Example3Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(new Date()).inner1Object(Inner1Object.builder()
                .age(1)
                .age1(2)
                .name("xsfda")
                .time(123L)
                .time1(12345L)
                .money(123.23213D)
                .money1(31.563D)
                .createDate(new Date())
                .inner2Object(Inner2Object.builder()
                        .age(2)
                        .age1(3)
                        .name("12312312")
                        .time(1256L)
                        .time1(1312L)
                        .money(12312.212D)
                        .money1(213.21312D)
                        .createDate(new Date())
                        .build())
                .build())
                .build(), false);
        System.out.println(document.toJson());
        Object object = deserializer.deserialize(document);
        System.out.println(object);
    }
}