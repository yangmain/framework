package com.rnkrsoft.framework.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 开发数据源注解，用于选择开始环境使用的数据源
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DataSource {
    /**
     * 数据源类型
     * @return 数据源类型
     */
    DataSourceType value() default DataSourceType.AUTO;
}
