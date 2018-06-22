package com.rnkrsoft.framework.orm.expression;

/**
 * Created by rnkesoft.com on 2018/4/16.
 * 表达式类型
 */
public enum ExpressionType {
    /**
     * 表达式就是输出的文本
     */
    TEXT,
    /**
     * 表达式为日期
     */
    DATE,
    /**
     * 表达式为序号
     */
    SEQ,
    /**
     * 随机数字
     */
    RANDOM;
}
