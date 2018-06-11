package com.rnkrsoft.framework.orm.mongo.convertor;

import com.rnkrsoft.framework.orm.mongo.Converter;
import com.rnkrsoft.time.DateStyle;
import com.rnkrsoft.utils.DateUtils;

/**
 * Created by rnkrsoft.com on 2018/6/11.
 */
public class DateConverter implements Converter {
    @Override
    public String as(Object value) {
        java.util.Date date = (java.util.Date) value;
        String str = DateUtils.formatJavaDate2String(date, DateStyle.FILE_FORMAT2);
        return str;
    }

    @Override
    public <T> T as(String value) {
        return (T) DateUtils.toDate(value);
    }
}
