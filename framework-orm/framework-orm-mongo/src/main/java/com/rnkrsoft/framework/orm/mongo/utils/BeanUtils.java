package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.MetaClass;
import com.rnkrsoft.reflection4j.MetaObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/10.
 */
public class BeanUtils {
    static class BeanSetting {

    }

    public static Map<String, Object> describe(Object value, BeanSetting setting) {
        if (value == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        MetaObject metaObject = GlobalSystemMetadata.forObject(value.getClass(), value);
        for (String getterName : metaObject.getGetterNames()) {
            Object val = metaObject.getValue(getterName);
            if (val instanceof String) {
                map.put(getterName, val);
            } else if (val instanceof Boolean) {
                map.put(getterName, val);
            } else if (val instanceof Integer) {
                map.put(getterName, val);
            } else if (val instanceof Long) {
                map.put(getterName, val);
            } else if (val instanceof Double) {
                map.put(getterName, val);
            } else if (val instanceof java.util.Date) {
                map.put(getterName, ((java.util.Date) val).getTime());
            } else if (val instanceof java.sql.Timestamp) {
                map.put(getterName, ((java.sql.Timestamp) val).getTime());
            } else {
                map.put(getterName, describe(val, setting));
            }
        }
        return map;
    }

    public static <T> T populate(Map<String, Object> value, Class<T> clazz, BeanSetting setting) {
        if (value == null) {
            return null;
        }
        MetaClass metaClass = GlobalSystemMetadata.forClass(clazz);
        T val = metaClass.newInstance();
        MetaObject metaObject = GlobalSystemMetadata.forObject(clazz, val);
        for (String name : value.keySet()) {
            Object value0 = value.get(name);
            if (value0 instanceof String) {
                metaObject.setValue(name, value0);
            } else if (value0 instanceof Boolean) {
                metaObject.setValue(name, value0);
            } else if (value0 instanceof Integer) {
                metaObject.setValue(name, value0);
            } else if (value0 instanceof Long) {
                metaObject.setValue(name, value0);
            } else if (value0 instanceof Double) {
                metaObject.setValue(name, value0);
            } else {
                Class setterClass = metaObject.getSetterType(name);
                Map map1 = (Map) value0;
                metaObject.setValue(name, populate(map1, setterClass, setting));
            }
        }
        return val;
    }
}
