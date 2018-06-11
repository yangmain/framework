package com.rnkrsoft.framework.orm.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by woate on 2018/6/11.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoColumn {
    /**
     * 字段名称
     *
     * @return 字段名称
     * @since 1.0.1
     */
    String name() default "";

    /**
     * 数据转换器
     * @return
     */
    Class converter() default Object.class;
    /**
     * 字段默认值
     * @return
     */
    String defaultValue() default "";
}
