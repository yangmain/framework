package com.rnkrsoft.framework.orm.mongo.example.example2;

import com.rnkrsoft.framework.orm.mongo.bson.BsonDeserializer;
import com.rnkrsoft.framework.orm.mongo.bson.BsonSerializer;
import com.rnkrsoft.framework.orm.mongo.example.example2.entity.Example2Entity;
import com.rnkrsoft.framework.orm.mongo.example.example2.entity.ObjectVO;
import com.rnkrsoft.utils.DateUtils;
import lombok.ToString;
import org.bson.Document;
import org.junit.Test;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2018/8/3.
 */
public class BsonDeserializerTest {
    @Test
    public void testSerialize() throws Exception {
        BsonSerializer<Example2Entity> serializer = new BsonSerializer(Example2Entity.class, true);
        BsonDeserializer<Example2Entity> deserializer = new BsonDeserializer(Example2Entity.class, true);
        Document document = serializer.serialize(Example2Entity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").createDate(DateUtils.toDate("2018/01/01 10:12:31")).objectVO(ObjectVO.builder()
                .age(1)
                .age1(2)
                .name("xsfda")
                .time(123L)
                .time1(12345L)
                .createDate(DateUtils.toDate("2018/08/02 21:12:31"))
                .build())
                .build(), false);
        System.out.println(document.toJson());
        Object object = deserializer.deserialize(document);
        System.out.println(object);
    }
}