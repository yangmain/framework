package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.ValueMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2018/6/11.
 * 标注在MongoDB实体上的字段
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
     * 是否允许为空
     * @return 是否允许空值
     */
    boolean nullable() default true;
    /**
     * 字段默认值
     * @return 字段默认值
     */
    String defaultValue() default "";

    /**
     * 作为条件时的逻辑模式
     * @return 作为条件时的逻辑模式
     */
    LogicMode logicMode() default LogicMode.AND;

    /**
     * 作为条件时值模式
     * @return 作为条件时值模式
     */
    ValueMode valueMode() default ValueMode.NONE;
}
