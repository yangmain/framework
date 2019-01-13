package com.rnkrsoft.framework.orm.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Incr {
    /**
     * 递增量，默认值1
     * @return 第增量
     */
    int increment() default 1;
}
