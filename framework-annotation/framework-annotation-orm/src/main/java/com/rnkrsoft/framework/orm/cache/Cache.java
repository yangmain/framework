package com.rnkrsoft.framework.orm.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 * 用于标记在有缓存功能的数据访问对象上
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    /**
     * 超时时间
     *
     * @return 超时时间
     */
    int expire() default -1;

    /**
     * 键值前缀
     * @return 键值前缀
     */
    String prefix() default "";

    /**
     * 数据库索引
     * @return 数据库索引
     */
    int index() default 1;
}
