package com.rnkrsoft.framework.orm.mongo.client;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import com.rnkrsoft.framework.orm.mongo.bson.BeanSetting;
import com.rnkrsoft.framework.orm.mongo.bson.BsonDeserializer;
import com.rnkrsoft.framework.orm.mongo.bson.BsonSerializer;
import com.rnkrsoft.framework.orm.mongo.utils.MongoEntityUtils;
import com.rnkrsoft.framework.sequence.spring.SequenceServiceConfigure;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Invoker;
import com.rnkrsoft.reflection4j.Reflector;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 * MongoDB数据库支持类
 * 1.新增
 * 2.选择性新增
 * 3.按照主键删除
 * 4.按照条件删除
 * 5.按照主键更新
 * 6.按照条件选择性更新
 * 7.按照条件全部进行更新
 * 8.按照主键查找
 * 9.按照条件查找
 * 10.按照条件分页查找
 * 11.按照条件统计条数
 */
@Slf4j
public class MongoDaoClient<Entity> {
    MongoClient mongoClient;
    Class<Entity> entityClass;
    Invoker primaryKeyInvoker;
    TableMetadata tableMetadata;
    String schema;
    String tableName;
    BsonSerializer<Entity> serializer;
    BsonSerializer<Entity> serializerNullable;
    BsonDeserializer<Entity> deserializer;
    @Setter
    SequenceServiceConfigure sequenceServiceConfigure;

    public MongoDaoClient(MongoClient mongoClient, String schema, Class<Entity> entityClass) {
        this.mongoClient = mongoClient;
        this.schema = schema;
        this.entityClass = entityClass;
        this.tableMetadata = MongoEntityUtils.extractTable(entityClass);
        Reflector reflector = GlobalSystemMetadata.reflector(entityClass);
        String primaryKey = tableMetadata.getPrimaryKeys().get(0);
        ColumnMetadata columnMetadata = tableMetadata.getColumnMetadataSet().get(primaryKey);
        this.primaryKeyInvoker = reflector.getGetter(columnMetadata.getJavaName());
        MongoTable mongoTable = this.entityClass.getAnnotation(MongoTable.class);
        if (mongoTable != null) {
            if (this.schema == null) {
                this.schema = mongoTable.schema();
            }
            if (this.tableName == null) {
                this.tableName = mongoTable.name();
            }
        } else {
            //TODO抛出异常
        }
        this.serializer = new BsonSerializer(BeanSetting.builder().nullable(false).sequenceServiceConfigure(sequenceServiceConfigure).tableMetadata(MongoEntityUtils.extractTable(entityClass)).build());
        this.serializerNullable = new BsonSerializer(BeanSetting.builder().nullable(true).sequenceServiceConfigure(sequenceServiceConfigure).tableMetadata(MongoEntityUtils.extractTable(entityClass)).build());
        this.deserializer = new BsonDeserializer(BeanSetting.builder().nullable(false).sequenceServiceConfigure(sequenceServiceConfigure).tableMetadata(MongoEntityUtils.extractTable(entityClass)).build());
    }

    MongoCollection<Document> getTable() {
        MongoCollection<Document> table = this.mongoClient.getDatabase(this.schema).getCollection(this.tableName);
        return table;
    }

    /**
     * 插入数据
     *
     * @param parameters
     */
    public void insert(Map<String, Object>... parameters) {
        if (parameters.length == 0) {

        } else if (parameters.length == 1) {
            Document document = new Document(parameters[0]);
            getTable().insertOne(document);
        } else {
            List<Document> documents = new ArrayList();
            for (Map<String, Object> parameter : parameters) {
                Document document = new Document(parameter);
                documents.add(document);
            }
            getTable().insertMany(documents);
        }
    }

    public void insertSelective(Entity... entities) {

        List<Map> list = new ArrayList();
        for (Entity entity : entities) {
            list.add(this.serializer.serialize(entity, false));
        }
        insert(list.toArray(new Map[list.size()]));
    }

    public void insert(Entity... entities) {
        List<Map> list = new ArrayList();
        for (Entity entity : entities) {
            list.add(this.serializerNullable.serialize(entity, false));
        }
        insert(list.toArray(new Map[list.size()]));
    }


    public long deleteByPrimaryKey(Entity entity) {
        String id = null;
        try {
            id = this.primaryKeyInvoker.invoke(entity, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DeleteResult result = null;
        if (tableMetadata.getColumn(tableMetadata.getPrimaryKeys().get(0)).getJavaType() == ObjectId.class) {
            result = getTable().deleteMany(new Document("_id", new ObjectId(id)));
        } else {
            result = getTable().deleteMany(new Document("_id", id));
        }
        return result.getDeletedCount();
    }

    /**
     * 删除符合条件的数据
     *
     * @param entity
     */
    public long delete(Object entity) {
        Document document = serializer.serialize(entity, true);
        if (log.isDebugEnabled()) {
            log.debug("condition :'{}'", document.toJson());
        }
        DeleteResult result = getTable().deleteMany(document);
        return result.getDeletedCount();
    }


    public long updateByPrimaryKeySelective(Object id, Entity entity) {
        Document conditionDocument = null;
        if (id instanceof ObjectId) {
            conditionDocument = new Document("_id", id);
        } else if (id instanceof String) {
            conditionDocument = new Document("_id", id);
        } else {
            throw ErrorContextFactory.instance().message("物理主键不为ObjectId或String").runtimeException();
        }
        Document valueDocument = serializer.serialize(entity, false);
        if (log.isDebugEnabled()) {
            log.debug("condition :'{}' update value:'{}'", conditionDocument.toJson(), valueDocument.toJson());
        }
        UpdateResult result = getTable().updateOne(conditionDocument, new Document("$set", valueDocument));
        return result.getModifiedCount();
    }

    public long updateByPrimaryKey(Object id, Entity entity) {
        Document conditionDocument = null;
        if (id instanceof ObjectId) {
            conditionDocument = new Document("_id", id);
        } else if (id instanceof String) {
            conditionDocument = new Document("_id", id);
        } else {
            throw ErrorContextFactory.instance().message("物理主键不为ObjectId或String").runtimeException();
        }
        Document valueDocument = serializerNullable.serialize(entity, false);
        if (log.isDebugEnabled()) {
            log.debug("condition :'{}' update value:'{}'", conditionDocument.toJson(), valueDocument.toJson());
        }
        UpdateResult result = getTable().updateOne(conditionDocument, new Document("$set", valueDocument));
        return result.getModifiedCount();
    }

    public long update(Object condition, Object entity) {
        Document conditionDocument = serializer.serialize(condition, true);
        Document valueDocument = serializerNullable.serialize(entity, false);
        if (log.isDebugEnabled()) {
            log.debug("condition :'{}' update value:'{}'", conditionDocument.toJson(), valueDocument.toJson());
        }
        UpdateResult result = getTable().updateMany(conditionDocument, new Document("$set", valueDocument));
        return result.getModifiedCount();
    }

    public long updateSelective(Entity condition, Entity entity) {
        Document conditionDocument = serializer.serialize(condition, true);
        Document valueDocument = serializer.serialize(entity, false);
        if (log.isDebugEnabled()) {
            log.debug("condition :'{}' update value:'{}'", conditionDocument.toJson(), valueDocument.toJson());
        }
        UpdateResult result = getTable().updateOne(conditionDocument, new Document("$set", valueDocument));
        return result.getModifiedCount();
    }

    public Entity selectByPrimaryKey(Object id) {
        Document document = null;
        if (id instanceof ObjectId) {
            document = new Document("_id", id);
        } else if (id instanceof String) {
            document = new Document("_id", id);
        } else {
            throw ErrorContextFactory.instance().message("物理主键不为ObjectId或String").runtimeException();
        }
        FindIterable fi = getTable().find(document);
        Iterator<Document> it = fi.iterator();
        if (it.hasNext()) {
            Document result = it.next();
            return deserializer.deserialize(result);
        } else {
            return null;
        }
    }

    public List<Entity> select(Entity entity) {
        List<Entity> list = new ArrayList();
        Document document = serializer.serialize(entity, true);
        FindIterable fi = getTable().find(document);
        Iterator<Document> it = fi.iterator();
        while (it.hasNext()) {
            Document result = it.next();
            list.add(deserializer.deserialize(result));
        }
        return list;
    }

    public Pagination<Entity> selectPage(Pagination<Entity> pagination, LogicMode logicMode) {
        //TODO
//        getTable().find(document).sort(document).limit()
        return null;
    }

    public long count(Entity entity) {
        Document document = serializer.serialize(entity, true);
        return getTable().count(document);
    }


}
