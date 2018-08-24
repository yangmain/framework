package com.rnkrsoft.framework.orm.mongo.example.example2;

import com.rnkrsoft.framework.orm.mongo.bson.BsonSerializer;
import com.rnkrsoft.framework.orm.mongo.example.example2.entity.Example2Entity;
import com.rnkrsoft.framework.orm.mongo.example.example2.entity.ObjectVO;
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
        BsonSerializer serializer = new BsonSerializer(Example2Entity.class, true);
        Document document = serializer.serialize(Example2Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(new Date()).objectVO(ObjectVO.builder()
                .age(1)
                .age1(2)
                .name("xsfda")
                .time(123L)
                .time1(12345L)
                .createDate(new Date())
                .build())
                .build(), true);
        System.out.println(document.toJson());
    }


    @Test
    public void testSerialize2() throws Exception {
        BsonSerializer serializer = new BsonSerializer(Example2Entity.class, true);
        Document document = serializer.serialize(Example2Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(new Date()).objectVO(ObjectVO.builder()
                .age(1)
                .age1(2)
                .name("xsfda")
                .time(123L)
                .time1(12345L)
                .createDate(new Date())
                .build())
                .build(), false);
        System.out.println(document.toJson());
    }
}