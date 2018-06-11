package com.rnkrsoft.framework.orm.mongo.convertor;

import com.rnkrsoft.framework.orm.mongo.Converter;

import java.math.BigDecimal;

/**
 * Created by rnkrsoft.com on 2018/6/11.
 */
public class FinanceDecimalConverter implements Converter{
    @Override
    public String as(Object value) {
        BigDecimal decimal = (BigDecimal)value;
        decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return decimal.toString();
    }

    @Override
    public <T> T as(String value) {
        return (T) new BigDecimal(value);
    }
}
