package com.rnkrsoft.framework.orm;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 物理主键生成策略
 */
public enum PrimaryKeyStrategy {
    /**
     * 自动选择
     * 如果字段类型为整形，则为IDENTITY
     * 如果字段类型为字符串，则为UUID
     * 其他类型抛出异常
     */
    AUTO,
    /**
     * UUID<br>
     * 支持字段类型：
     * 1.字符串
     */
    UUID,
    /**
     * 序列服务<br>
     * 支持字段类型：
     * 1.字符串
     * 2.整型或长整型
     */
    SEQUENCE_SERVICE,
    /**
     * 数据库的自动增长字段
     */
    IDENTITY
}
