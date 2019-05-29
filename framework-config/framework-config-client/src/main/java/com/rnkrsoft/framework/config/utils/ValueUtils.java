package com.rnkrsoft.framework.config.utils;

import com.rnkrsoft.utils.DateUtils;

import java.math.BigDecimal;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
public class ValueUtils {
    public static <T> T convert(String value, Class<T> type){
        if (type == String.class){
            return (T) value;
        }
        if (type == Boolean.TYPE || type == Boolean.class){
            Boolean val = Boolean.valueOf(value);
            return (T) val;
        }
        if (type == Byte.TYPE || type == Byte.class){
            Byte val = Byte.valueOf(value);
            return (T) val;
        }
        if (type == Short.TYPE || type == Short.class){
            Short val = Short.valueOf(value);
            return (T) val;
        }
        if (type == Integer.TYPE || type == Integer.class){
            Integer val = Integer.valueOf(value);
            return (T) val;
        }
        if (type == Long.TYPE || type == Long.class){
            Long val = Long.valueOf(value);
            return (T) val;
        }
        if (type == Float.TYPE || type == Float.class){
            Float val = Float.valueOf(value);
            return (T) val;
        }
        if (type == Double.TYPE || type == Double.class){
            Double val = Double.valueOf(value);
            return (T) val;
        }
        if (type == java.util.Date.class){
            java.util.Date val = DateUtils.toDate(value);
            return (T) val;
        }
        if (type == BigDecimal.class){
            BigDecimal val = new BigDecimal(value);
            return (T) val;
        }
        return (T) value;
    }
}
