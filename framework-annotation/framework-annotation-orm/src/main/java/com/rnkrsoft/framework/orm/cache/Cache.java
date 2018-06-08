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
     * @return
     */
    int expire() default -1;

    int db() default 1;
}
