package com.rnkrsoft.framework.test;

/**
 * Created by rnkrsoft on 2017/1/4.
 * 测试时使用的表名模式
 */
public enum TableNameMode {
    /**
     * 自动识别
     */
    AUTO,
    /**
     * 使用实体上的
     */
    ENTITY,
    /**
     * 使用@CreateTable上的
     */
    CREATE_TEST
}
