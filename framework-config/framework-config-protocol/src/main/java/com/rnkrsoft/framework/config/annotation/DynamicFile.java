package com.rnkrsoft.framework.config.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicFile {
    /**
     * 文件路径
     * @return 文件路径
     */
    String value();
    /**
     * 是否加密
     * @return 是否加密
     * @since 1.1.0
     */
    boolean encrypt() default false;
    /**
     * 文件描述
     * @return 文件描述
     */
    String desc();
}