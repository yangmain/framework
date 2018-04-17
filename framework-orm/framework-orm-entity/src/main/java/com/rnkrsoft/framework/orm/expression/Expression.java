package com.rnkrsoft.framework.orm.expression;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/4/17.
 */
@Getter
@ToString
public class Expression {
    String expression;
    Token[] tokens = {};

    public Expression(String expression) {
        this.expression = expression;
    }

    public void setTokens(List<Token> tokens)  {
        this.tokens = tokens.toArray(new Token[tokens.size()]);
    }

}
