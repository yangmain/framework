package com.rnkrsoft.framework.orm.mongo.spring;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Invoker;
import com.rnkrsoft.reflection4j.MetaClass;
import com.rnkrsoft.reflection4j.Reflector;
import com.rnkrsoft.reflection4j.invoker.GetFieldInvoker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.Table;
import com.rnkrsoft.framework.orm.ValueBy;
import com.rnkrsoft.framework.orm.ValueByColumn;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.extractor.OrmEntityExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.dao.support.DaoSupport;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
    String tableName;

    String getTableName() {
        if (this.tableName == null) {
            Table table = (Table) entityClass.getAnnotation(Table.class);
            return table.name();
        } else {
            return this.tableName;
        }
    }

    public MongoDaoSupport(MongoDatabase database, Class<Entity> entityClass) {
        this.database = database;
        this.entityClass = entityClass;
        Reflector reflector = GlobalSystemMetadata.reflector(entityClass);
        TableMetadata tableMetadata = new EntityExtractorHelper().extractTable(entityClass, true);
        String primaryKey = tableMetadata.getPrimaryKeys().get(0);
        ColumnMetadata columnMetadata = tableMetadata.getColumnMetadataSet().get(primaryKey);
        this.primaryKeyInvoker = reflector.getGetter(columnMetadata.getJavaName());
    }

    public MongoDaoSupport(MongoDatabase database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    MongoCollection<Document> getTable() {
        MongoCollection<Document> table = this.database.getCollection(getTableName());
        return table;
    }

    /**
     * 插入数据
     *
     * @param entities
     */
    public void insert(Entity... entities) {
        if (entities.length == 0) {

        }
        List<Document> documents = new ArrayList();
        for (Entity entity : entities) {
            try {
                Map parameter = BeanUtils.describe(entity);
                Document document = new Document(parameter);
                documents.add(document);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        getTable().insertMany(documents);
    }

    /**
     * 插入非null字段
     *
     * @param entities
     */
    public void insertSelective(Entity... entities) {
        if (entities.length == 0) {

        }
        List<Document> documents = new ArrayList();
        for (Entity entity : entities) {
            try {
                Map parameter = BeanUtils.describe(entity);
                Document document = new Document(parameter);
                documents.add(document);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        getTable().insertMany(documents);
    }

    /**
     * 删除符合条件的数据
     *
     * @param entity
     */
    public void delete(Entity entity) {
        ValueBy valueBy = (ValueBy) entity;
        Map<String, ValueByColumn> valueByMap = valueBy.getValueByMap();
        for (ValueByColumn valueByColumn : valueByMap.values()) {

        }
        Bson filters = Filters.eq("", "");
        getTable().deleteMany(filters);
    }

    public void deleteByPrimaryKey(Entity entity) {
        ValueBy valueBy = (ValueBy) entity;
        String id = null;
        try {
            id = this.primaryKeyInvoker.invoke(entity, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getTable().deleteMany(Filters.eq("_id", id));
    }

    public void updateByPrimaryKey(Entity condition, Entity entity) {
        try {
            Map conditionMap = BeanUtils.describe(condition);
            Map entityMap = BeanUtils.describe(entity);
            updateByPrimaryKey(conditionMap, entityMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void updateByPrimaryKey(Map<String, Object> condition, Map<String, Object> parameter) {

    }

    public void updateByPrimaryKeySelective(Entity condition, Entity entity) {
        try {
            Map conditionMap = BeanUtils.describe(condition);
            Map entityMap = BeanUtils.describe(entity);
            //TODO 将为null的值去除
            updateByPrimaryKey(conditionMap, entityMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public List<Entity> select(Entity entity) {
        try {
            Map entityMap = BeanUtils.describe(entity);
            //TODO 将为null的值去除
            List<Map> result = select(entityMap);
            List<Entity> list = new ArrayList();
            MetaClass metaClass = GlobalSystemMetadata.forClass(entityClass);
            for (Map map : result) {
                Entity object = metaClass.newInstance();
                BeanUtils.populate(object, map);
                list.add(object);
            }
            return list;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map> select(Map<String, Object> parameter) {

        System.out.println(parameter);
        return null;
    }

    public Pagination<Entity> selectPage(Pagination<Entity> pagination) {
        return null;
    }

    public long count(Entity entity) {
        try {
            Map entityMap = BeanUtils.describe(entity);
            //TODO 将为null的值去除
            return count(entityMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long count(Map<String, Object> parameter) {
        return 0L;
    }


    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        if (database == null) {
            throw ErrorContextFactory.instance().message("MongoDB 数据源未配置").runtimeException();
        }
    }
}
