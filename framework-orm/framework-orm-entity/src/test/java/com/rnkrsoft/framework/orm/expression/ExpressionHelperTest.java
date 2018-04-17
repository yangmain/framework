package com.rnkrsoft.framework.orm.expression;

import org.junit.Test;

/**
 * Created by rnhrsoft.com on 2018/4/11.
 */
public class ExpressionHelperTest {

    @Test
    public void testParse() throws Exception {
        Expression expression = ExpressionHelper.parse("ABCD${yyyyMMddHHmmssSSS}${seqNo:5}x${seqNo:8}EFGH", false);
    }


    @Test
    public void testEval() throws Exception {
        ExpressionContext ctx = ExpressionContext.builder().expression("ABCD${yyyyMMddHHmmssSSS}${seqNo:5}x${seqNo:8}EFGH").useCache(true).build();
//        for (int i = 0; i < 10000; i++) {
            String value = ExpressionHelper.eval(ctx);

//        }
        System.out.println(value);
    }
}