package com.rnkrsoft.framework.orm.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 日期字段类型
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateColumn {
    /**
     * 字段名称
     *
     * @return 字段名称
     * @since 1.0.0
     */
    String name() default "";

    /**
     * 是否允许为空
     *
     * @return 是否允许为空
     * @since 1.0.0
     */
    boolean nullable() default true;

    /**
     * 默认值
     * @return 默认值
     * @since 1.0.0
     */
    String defaultValue() default "";

    /**
     * 字段数据类型
     *
     * @return 数据类型
     * @since 1.0.0
     */
    DateType type() default DateType.AUTO;
}
