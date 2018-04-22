package com.rnkrsoft.framework.orm;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 测试时使用的表名模式
 */
public enum NameMode {
    /**
     * 自动识别
     */
    auto,
    /**
     * 使用实体上的
     */
    entity,
    /**
     * 使用@CreateTable上的
     */
    createTest
}
