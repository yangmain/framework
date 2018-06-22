package com.rnkrsoft.framework.orm.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2018/6/11.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoTable {
    /**
     * 表名
     * @return 表名
     * @since 1.0.1
     */
    String name() default "";
    /**
     * 实体所属模式
     * @return 模式
     * @since 1.0.1
     */
    String schema() default "";

}
