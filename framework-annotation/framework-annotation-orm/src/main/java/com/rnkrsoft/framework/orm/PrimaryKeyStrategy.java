package com.rnkrsoft.framework.orm;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 物理主键生成策略
 */
public enum PrimaryKeyStrategy {
    /**
     * 自动选择
     */
    AUTO,
    /**
     * UUID
     */
    UUID,
    /**
     * 序列服务
     */
    SEQUENCE_SERVICE,
    DataEngineType, /**
     * 数据库的自动增长字段
     */
    IDENTITY
}
