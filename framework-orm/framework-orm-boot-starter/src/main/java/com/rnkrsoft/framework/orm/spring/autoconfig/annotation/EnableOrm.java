package com.rnkrsoft.framework.orm.spring.autoconfig.annotation;

import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.spring.autoconfig.OrmRegister;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *  Created by rnkrsoft.com on 2018/2/10.
 *  用于标注在启动类上
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(OrmRegister.class)
public @interface EnableOrm {
    /**
     * 配置对象类
     * @return
     */
    Class<OrmConfig> config();
}