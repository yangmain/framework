package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.utils.BeanUtils;
import com.rnkrsoft.framework.orm.mongo.utils.BsonUtils;
import com.rnkrsoft.framework.orm.mongo.utils.MongoEntityUtils;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Invoker;
import com.rnkrsoft.reflection4j.Reflector;
import org.bson.Document;
import org.springframework.dao.support.DaoSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 * MongoDB数据库支持类
 */
public abstract class MongoDaoSupport<Entity> extends DaoSupport {
    MongoDatabase database;
    Class<Entity> entityClass;
    Invoker primaryKeyInvoker;
    TableMetadata tableMetadata;

    String getTableName() {
        return tableMetadata.getTableName();
    }

    public MongoDaoSupport(MongoDatabase database, Class<Entity> entityClass) {
        this.database = database;
        this.entityClass = entityClass;
        this.tableMetadata = MongoEntityUtils.extractTable(entityClass);
        Reflector reflector = GlobalSystemMetadata.reflector(entityClass);
        String primaryKey = tableMetadata.getPrimaryKeys().get(0);
        ColumnMetadata columnMetadata = tableMetadata.getColumnMetadataSet().get(primaryKey);
        this.primaryKeyInvoker = reflector.getGetter(columnMetadata.getJavaName());
    }

    MongoCollection<Document> getTable() {
        MongoCollection<Document> table = this.database.getCollection(getTableName());
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

    public void insert(boolean nullable, Entity... entities) {
        List<Map> list = new ArrayList();
        for (Entity entity : entities) {
            list.add(BeanUtils.describe(entity, BeanUtils.BeanSetting.builder().nullable(nullable).build()));
        }
        insert(list.toArray(new Map[list.size()]));
    }

    /**
     * 删除符合条件的数据
     *
     * @param entity
     */
    public void delete(Object entity) {
        getTable().deleteMany(BsonUtils.and(entity, false));
    }

    public void deleteByPrimaryKey(Entity entity) {
        String id = null;
        try {
            id = this.primaryKeyInvoker.invoke(entity, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getTable().deleteMany(Filters.eq("_id", id));
    }

    public void updateByPrimaryKey(Entity condition, Entity entity) {
        getTable().updateMany(BsonUtils.and(condition, false), new Document(BeanUtils.describe(entity, BeanUtils.BeanSetting.builder().nullable(true).build())));
    }

    public void updateByPrimaryKey(Map<String, Object> condition, Map<String, Object> parameter) {
        getTable().updateMany(new Document(condition), new Document(parameter));
    }

    public void updateByPrimaryKeySelective(Entity condition, Entity entity) {
        getTable().updateMany(BsonUtils.and(condition, false), new Document(BeanUtils.describe(entity, BeanUtils.BeanSetting.builder().nullable(false).build())));
    }

    public List<Entity> select(Entity entity) {
        List<Entity> list = new ArrayList();
        FindIterable fi = getTable().find(BsonUtils.and(entity, false));
        Iterator<Document> it = fi.iterator();
        while (it.hasNext()) {
            Document document = it.next();
            list.add(BeanUtils.populate(document, entityClass, BeanUtils.BeanSetting.builder().nullable(true).build()));
        }
        return list;
    }

    public Pagination<Entity> selectPage(Pagination<Entity> pagination) {
        Entity entity = pagination.getEntity();
        Document document = BsonUtils.and(entity, false);
//        getTable().
        return null;
    }

    public long count(Entity entity) {
        Document document = BsonUtils.and(entity, false);
        return getTable().count(document);
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        if (database == null) {
            throw ErrorContextFactory.instance().message("MongoDB 数据源未配置").runtimeException();
        }
    }
}
