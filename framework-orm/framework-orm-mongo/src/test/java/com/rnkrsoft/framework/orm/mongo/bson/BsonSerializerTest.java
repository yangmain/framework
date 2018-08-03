package com.rnkrsoft.framework.orm.mongo.bson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.rnkrsoft.framework.orm.mongo.entity.OperateLogEntity;
import org.bson.Document;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/8/3.
 */
public class BsonSerializerTest {

    @Test
    public void testSerialize() throws Exception {
        BsonSerializer serializer = new BsonSerializer(OperateLogEntity.class, false);
        Document document = serializer.serialize(OperateLogEntity.builder().age(12).data("sssss").id(UUID.randomUUID().toString()).name("mike").build(), true);
        System.out.println(document.toJson());
        DBObject queryCondition = new BasicDBObject();
        //agender=female OR age<=23
        queryCondition = new BasicDBObject();
        queryCondition.put("_id",UUID.randomUUID().toString());
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("DATA", new BasicDBObject("$lte", "sssss")));
        queryCondition.put("$or", values);
        queryCondition.put("AGE", new BasicDBObject("$gt", 12));
        Document document0 = new Document(queryCondition.toMap());
        System.out.println(document0.toJson());
    }
}