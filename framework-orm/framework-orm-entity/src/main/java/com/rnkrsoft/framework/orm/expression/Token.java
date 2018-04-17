package com.rnkrsoft.framework.orm.expression;

import lombok.Data;

@Data
class Token {
    /**
     * 表达式
     */
    String expression;
    /**
     * 表达式类型
     */
    ExpressionType type = ExpressionType.TEXT;
    /**
     * 表达式结果长度，如果为-1，不进行处理，如果为大于0的数，则进行填充到指定长度
     */
    int length;

    public Token(String expression, int length, ExpressionType type) {
        this.expression = expression;
        this.length = length;
        this.type = type;
    }
}