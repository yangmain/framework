package com.rnkrsoft.framework.orm.mongo;

/**
 * Created by rnkrsoft on 2018/6/11.
 */
public interface Converter {
    /**
     * 将对象转换为字符串字面量
     * @param value
     * @return
     */
    String as(Object value);

    /**
     * 将字符串字面量转换为对象
     * @param value
     * @param <T>
     * @return
     */
    <T> T as(String value);
}
