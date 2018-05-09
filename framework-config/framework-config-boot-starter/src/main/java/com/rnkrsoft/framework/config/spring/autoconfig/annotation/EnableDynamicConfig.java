package com.rnkrsoft.framework.config.spring.autoconfig.annotation;

import com.rnkrsoft.framework.config.spring.autoconfig.DynamicConfigRegistrar;
import com.rnkrsoft.framework.config.v1.RuntimeMode;
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
@Import(DynamicConfigRegistrar.class)
public @interface EnableDynamicConfig {
    /**
     * 配置中心服务器
     * @return 配置中心服务器
     */
    String host() default "localhost";

    /**
     * 配置中心服务器端口
     * @return 配置中心服务器端口
     */
    int port() default 80;

    /**
     * 项目对应的组织编号
     * @return 项目对应的组织编号
     */
    String groupId();

    /**
     * 项目对应的组件编号
     * @return 项目对应的组件编号
     */
    String artifactId();

    /**
     * 项目对应的版本号
     * @return 项目对应的版本号
     */
    String version() default "1.0.0";

    /**
     * 项目运行环境
     * @return 项目运行环境
     */
    String env() default "release";

    /**
     * 客户端初始化后拉取配置的延时时间（秒）
     * @return 客户端初始化后拉取配置的延时时间（秒）
     */
    int fetchDelaySeconds() default 30;

    /**
     * 客户端拉取配置的间隔（秒）
     * @return 客户端拉取配置的间隔（秒）
     */
    int fetchIntervalSeconds() default 30;

    /**
     * 配置模式，AUTO自动模式，先从本地加载，然后使用服务器的进行覆盖;LOCAL从本地配置文件加载，REMOTE从服务器加载
     * @return 配置模式
     */
    RuntimeMode mode() default RuntimeMode.AUTO;

    /**
     * 远程配置缓存根目录
     * @return 远程配置缓存根目录
     */
    String workHome() default "~/config";
}