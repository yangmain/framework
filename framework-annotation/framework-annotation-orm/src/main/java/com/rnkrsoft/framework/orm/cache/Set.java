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
public @interface Set {
    /**
     * 超时时间，0为永远不超时
     * @return 超时时间
     */
    int expire() default 0;
}
