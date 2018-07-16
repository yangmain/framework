package com.rnkrsoft.framework.messagequeue.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by woate on 2018/7/14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Message {
    /**
     * 最大年龄
     * @return
     */
    int maxAge() default 3;
    /**
     * 路由关键字
     * @return
     */
    String routingKey() default "*";
}
