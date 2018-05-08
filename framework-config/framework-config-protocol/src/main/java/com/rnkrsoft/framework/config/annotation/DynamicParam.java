package com.rnkrsoft.framework.config.annotation;

import com.rnkrsoft.framework.config.v1.ParamType;

import java.lang.annotation.*;

/**
 * Created by rnkrsoft.com on 2018/3/9.
 * 20:58
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicParam {
    /**
     * 参数名，当参数名为空时，使用标注注解所在类+.+字段名为参数名
     * @return 参数名
     */
    String value() default "";

    /**
     * 参数类型
     * @return 参数类型
     */
    ParamType type() default ParamType.SYSTEM;

    /**
     * 是否复制到系统属性中
     * @return 是否复制到系统属性中
     */
    boolean systemProperty() default false;
    /**
     * 是否加密
     * @return 是否加密
     */
    boolean encrypt() default false;
    /**
     * 参数描述
     * @return 参数描述
     */
    String desc();
}