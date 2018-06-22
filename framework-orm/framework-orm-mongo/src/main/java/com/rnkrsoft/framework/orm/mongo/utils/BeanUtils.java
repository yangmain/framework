package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mongo.MongoPrimaryKeyHelper;
import com.rnkrsoft.framework.sequence.spring.SequenceServiceConfigure;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.MetaClass;
import com.rnkrsoft.reflection4j.MetaObject;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/10.
 */
@Slf4j
public class BeanUtils {
    @Getter
    @Builder
    @ToString
    public static class BeanSetting {
        /**
         * 是否允许null值
         */
        boolean nullable;
        TableMetadata tableMetadata;
        SequenceServiceConfigure sequenceServiceConfigure;

        public BeanSetting() {
            this.nullable = true;
        }

        public BeanSetting(boolean nullable, TableMetadata tableMetadata, SequenceServiceConfigure sequenceServiceConfigure) {
            this.nullable = nullable;
            this.tableMetadata = tableMetadata;
            this.sequenceServiceConfigure = sequenceServiceConfigure;
        }
    }

    public static Map<String, Object> describe(Object value, BeanSetting setting) {
        if (value == null) {
            return null;
        }
        Map<String, Object> map = new HashMap();
        MetaObject metaObject = GlobalSystemMetadata.forObject(value.getClass(), value);
        TableMetadata tableMetadata = setting.getTableMetadata();
        for (String getterName : metaObject.getGetterNames()) {
            Object val = metaObject.getValue(getterName);
            String columnName = getterName;
            if (tableMetadata != null){
                ColumnMetadata columnMetadata = tableMetadata.getColumnByJavaName(getterName);
                columnName = columnMetadata.getJdbcName();
                if (columnMetadata.isPrimaryKey()) {
                    if (val == null) {
                        if (setting.getSequenceServiceConfigure() != null) {
                            val = MongoPrimaryKeyHelper.generate(setting.getSequenceServiceConfigure(), tableMetadata, columnMetadata);
                        } else {
                            log.error("请配置 SequenceServiceConfigure");
                        }
                    }
                }
            }
            //不允许空值，则直接跳过
            if (val == null) {
                if (setting.isNullable()) {
                    map.put(getterName, null);
                }
                continue;
            }
            if (val instanceof String) {
                map.put(columnName, val);
            } else if (val instanceof Boolean) {
                map.put(columnName, val);
            } else if (val instanceof Integer) {
                map.put(columnName, val);
            } else if (val instanceof Long) {
                map.put(columnName, val);
            } else if (val instanceof Double) {
                map.put(columnName, val);
            } else if (val instanceof java.util.Date) {
                map.put(columnName, ((java.util.Date) val).getTime());
            } else if (val instanceof java.sql.Timestamp) {
                map.put(columnName, ((java.sql.Timestamp) val).getTime());
            } else if (val instanceof Serializable) {
                map.put(columnName, describe(val, setting));
            } else {
                throw ErrorContextFactory.instance().message("'{}' 不支持的数据类型 '{}'", val, val.getClass()).runtimeException();
            }
        }
        return map;
    }

    public static <T> T populate(Map<String, Object> map, Class<T> clazz, BeanSetting setting) {
        if (map == null) {
            return null;
        }
        MetaClass metaClass = GlobalSystemMetadata.forClass(clazz);
        T val = metaClass.newInstance();
        MetaObject metaObject = GlobalSystemMetadata.forObject(clazz, val);
        TableMetadata tableMetadata = setting.getTableMetadata();
        for (String name : map.keySet()) {
            Object value0 = map.get(name);
            //不允许空值，则直接跳过
            if (value0 == null) {
                if (setting.isNullable()) {

                }
                continue;
            }
            String javaName = name;
            if (tableMetadata != null) {
                ColumnMetadata columnMetadata = tableMetadata.getColumn(name);
                javaName = columnMetadata.getJavaName();
            }
            if (!metaObject.hasSetter(javaName)) {
                continue;
            }
            if (value0 instanceof String) {
                metaObject.setValue(javaName, value0);
            } else if (value0 instanceof Boolean) {
                metaObject.setValue(javaName, value0);
            } else if (value0 instanceof Integer) {
                metaObject.setValue(javaName, value0);
            } else if (value0 instanceof Long) {
                metaObject.setValue(javaName, value0);
            } else if (value0 instanceof Double) {
                metaObject.setValue(javaName, value0);
            } else if (value0 instanceof Map) {
                Class setterClass = metaObject.getSetterType(javaName);
                if (Serializable.class.isAssignableFrom(setterClass)) {
                    Map map1 = (Map) value0;
                    metaObject.setValue(javaName, populate(map1, setterClass, setting));
                } else {
                    throw ErrorContextFactory.instance().message("'{}' 未实现 '{}'", setterClass, Serializable.class).runtimeException();
                }
            } else {
                log.warn("不支持的'{}' 数据类型: '{}'", value0, value0.getClass());
                continue;
            }
        }
        return val;
    }
}
