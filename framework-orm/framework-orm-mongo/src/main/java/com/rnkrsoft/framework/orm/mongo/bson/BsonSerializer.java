package com.rnkrsoft.framework.orm.mongo.bson;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.MongoLogicMode;
import com.rnkrsoft.framework.orm.mongo.MongoValueMode;
import com.rnkrsoft.framework.orm.mongo.utils.MongoEntityUtils;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.MetaObject;
import org.bson.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by rnkrsoft.com on 2018/8/3.
 */
public class BsonSerializer<T> {
    BeanSetting beanSetting;

    public BsonSerializer(BeanSetting setting) {
        this.beanSetting = setting;
    }

    public BsonSerializer(Class<T> entityClass, boolean nullable) {
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(entityClass);
        this.beanSetting = BeanSetting.builder().tableMetadata(tableMetadata).nullable(nullable).build();
    }

    public Document serialize(Object object, boolean condition,Class entityClass0, BeanSetting setting0) {
        BeanSetting setting = setting0 != null ? setting0 : this.beanSetting;
        Class entityClass = entityClass0 != null ? entityClass0 : setting0.getTableMetadata().getEntityClass();
        TableMetadata tableMetadata = null;
        if (entityClass0 != null){
            tableMetadata = MongoEntityUtils.extractTable(entityClass);
        }else{
            tableMetadata = setting.getTableMetadata();
        }

        Document document = new Document();
        MetaObject metaObject = GlobalSystemMetadata.forObject(entityClass, object);
        for (String columnName : tableMetadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = tableMetadata.getColumn(columnName);
            Object value = metaObject.getValue(columnMetadata.getJavaName());
            if (columnMetadata == null) {
                continue;
            }
            if (condition) {
                ValueMode valueMode = columnMetadata.getValueMode();
                LogicMode logicMode = columnMetadata.getLogicMode();
                //如果值为null,配置不允许为null,则不将值序列化
                if (value == null) {
                    if (setting.isNullable()){
                        document.append(columnName, value);
                    }else{
                        continue;
                    }
                }
                if (value.getClass() == Integer.TYPE || value.getClass() == Integer.class
                        || value.getClass() == Double.TYPE || value.getClass() == Double.class
                        || value.getClass() == Long.TYPE || value.getClass() == Long.class
                        || value.getClass() == String.class
                        ) {
                    if (logicMode == LogicMode.OR) {
                        List orList = (List) document.get(MongoLogicMode.OR.getCode());
                        if (orList == null) {
                            orList = new ArrayList();
                            document.append(MongoLogicMode.OR.getCode(), orList);
                        }
                        if (valueMode == ValueMode.EQUAL) {
                            orList.add(new Document(columnName, value));
                        } else {
                            orList.add(new Document(columnName, new Document(MongoValueMode.valueOfCode(valueMode).getCode(), value)));
                        }
                    } else {
                        if (valueMode == ValueMode.EQUAL) {
                            document.append(columnName, value);
                        } else {
                            document.append(columnName, new Document(MongoValueMode.valueOfCode(valueMode).getCode(), value));
                        }
                    }
                }else{
                    throw ErrorContextFactory.instance()
                            .message("Mongo查询条件不支持 '{}'", value.getClass())
                            .solution("检查对象'{}'", value.getClass())
                            .runtimeException();
                }
            } else {
                if (value == null && !columnMetadata.getDefaultValue().isEmpty()) {
                    value = columnMetadata.getDefaultValue();
                }
                if (value == null){
                    if (setting.isNullable()){
                        document.append(columnName, value);
                    }else{
                        continue;
                    }
                }
                if (value.getClass() == Integer.TYPE || value.getClass() == Integer.class
                        || value.getClass() == Double.TYPE || value.getClass() == Double.class
                        || value.getClass() == Long.TYPE || value.getClass() == Long.class
                        || value.getClass() == String.class
                        ) {
                    document.append(columnName, value);
                } else if (Serializable.class.isAssignableFrom(value.getClass())) {
                    document.append(columnName, serialize(value, condition, value.getClass(), BeanSetting.builder().nullable(beanSetting.nullable).sequenceServiceConfigure(beanSetting.sequenceServiceConfigure).tableMetadata(MongoEntityUtils.extractTable(value.getClass())).build()));
                }else{
                    throw ErrorContextFactory.instance()
                            .message("Mongo不支持 '{}'", value.getClass())
                            .solution("检查对象'{}'", value.getClass())
                            .runtimeException();
                }

            }
        }
        return document;
    }

    public Document serialize(Object object, boolean condition) {
        return serialize(object, condition, beanSetting.getTableMetadata().getEntityClass(), beanSetting);
    }
}
