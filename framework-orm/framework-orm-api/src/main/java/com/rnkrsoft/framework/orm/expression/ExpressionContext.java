package com.rnkrsoft.framework.orm.expression;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/4/17.
 */
@Data
@Builder
@ToString
public class ExpressionContext {
    boolean useCache;
    /**
     * 表达式
     */
    String expression;
    /**
     * 序列实现类名
     */
    String sequenceClassName;
    /**
     * 数据库模式
     */
    String schema;
    /**
     * 前缀
     */
    String prefix;
    /**
     * 序列名，一般情况下为表名
     */
    String sequenceName;
    /**
     * 序列服务生成特征
     */
    String feature;
}
