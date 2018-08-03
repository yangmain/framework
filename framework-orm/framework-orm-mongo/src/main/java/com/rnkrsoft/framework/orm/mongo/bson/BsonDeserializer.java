package com.rnkrsoft.framework.orm.mongo.bson;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.MongoLogicMode;
import com.rnkrsoft.framework.orm.mongo.utils.MongoEntityUtils;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.MetaClass;
import com.rnkrsoft.reflection4j.MetaObject;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/8/3.
 */
@Slf4j
public class BsonDeserializer<T> {
    BeanSetting beanSetting;

    public BsonDeserializer(BeanSetting beanSetting) {
        this.beanSetting = beanSetting;
    }

    public BsonDeserializer(Class<T> entityClass, boolean nullable) {
        TableMetadata tableMetadata = MongoEntityUtils.extractTable(entityClass);
        this.beanSetting = BeanSetting.builder().tableMetadata(tableMetadata).nullable(nullable).build();
    }

    T deserialize(Map<String, Object> map, Class entityClass0, BeanSetting setting0) {
        BeanSetting setting = setting0 != null ? setting0 : this.beanSetting;
        TableMetadata tableMetadata = null;
        Class entityClass = entityClass0 != null ? entityClass0 : setting0.getTableMetadata().getEntityClass();
        if (entityClass0 != null){
            tableMetadata = MongoEntityUtils.extractTable(entityClass);
        }else{
            tableMetadata = setting.getTableMetadata();
        }
        MetaClass metaClass = GlobalSystemMetadata.forClass(entityClass);
        T val = metaClass.newInstance();
        MetaObject metaObject = GlobalSystemMetadata.forObject(entityClass, val);
        for (String columnName : map.keySet()) {
            if (columnName.equals(MongoLogicMode.OR.getCode())) {
                throw ErrorContextFactory.instance()
                        .message("当前数据为MongoDB 条件数据，无法映射到JavaBean")
                        .solution("检查是否数据异常")
                        .runtimeException();
            }
            Object value0 = map.get(columnName);
            //不允许空值，则直接跳过
            if (value0 == null) {
                if (!setting.isNullable()) {
                    continue;
                }
            }
            ColumnMetadata columnMetadata = tableMetadata.getColumn(columnName);
            String javaName = columnMetadata.getJavaName();
            if (!metaObject.hasSetter(javaName)) {
                throw ErrorContextFactory.instance()
                        .message("表'{}'字段'{}'数据异常，无对应的实体与之映射", tableMetadata.getTableName(), columnName)
                        .solution("检查是否数据异常")
                        .runtimeException();
            }
            Class javaType = metaObject.getSetterType(javaName);
            if (javaType == String.class) {
                metaObject.setValue(javaName, StringUtils.safeToString(value0));
            } else if (javaType == Boolean.class || javaType == Boolean.TYPE) {
                metaObject.setValue(javaName, Boolean.valueOf(StringUtils.safeToString(value0, "false")));
            } else if (javaType == Integer.class || javaType == Integer.TYPE) {
                metaObject.setValue(javaName, Integer.valueOf(StringUtils.safeToString(value0, "0")));
            } else if (javaType == Long.class || javaType == Long.TYPE) {
                metaObject.setValue(javaName, Long.valueOf(StringUtils.safeToString(value0, "0")));
            } else if (javaType == Double.class || javaType == Double.TYPE) {
                metaObject.setValue(javaName, Double.valueOf(StringUtils.safeToString(value0, "0")));
            } else if (javaType == BigDecimal.class) {
                metaObject.setValue(javaName, new BigDecimal(StringUtils.safeToString(value0, "0")));
            } else if (javaType == Date.class) {
                Date date = null;
                if (value0 instanceof Long) {
                    date = new Date(Long.valueOf(StringUtils.safeToString(value0, "0")));
                } else if (value0 instanceof String) {
                    date = DateUtils.toDate(StringUtils.safeToString(value0, "1971/01/01 00:00:00"));
                } else {
                    throw ErrorContextFactory.instance().message("'{}'无效日期", value0).runtimeException();
                }
                metaObject.setValue(javaName, date);
            } else if (Serializable.class.isAssignableFrom(javaType)) {
                Document document = (Document) value0;
                metaObject.setValue(javaName, deserialize(document, javaType, setting));
            } else {
                log.warn("不支持的'{}' 数据类型: '{}'", value0, value0.getClass());
                continue;
            }
        }
        return val;
    }

    public T deserialize(Document document) {
        return deserialize(document, beanSetting.getTableMetadata().getEntityClass(), beanSetting);
    }
}
