package com.rnkrsoft.framework.orm.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rnhrsoft.com on 2018/4/11.
 */
public class ExpressionHelperTest {

    @Test
    public void testParse() throws Exception {
        Expression expression = ExpressionHelper.parse("ABCD_${yyyyMMddHHmmssSSS}_${SEQ:5}_${SEQ:8}_${RANDOM:8}_EFGH", false);
    }


    @Test
    public void testEval() throws Exception {
        ExpressionContext ctx = ExpressionContext.builder().expression("ABCD_${yyyyMMddHHmmssSSS}_${SEQ:5}_${SEQ:8}_${RANDOM:8}_EFGH").useCache(true).build();
        String value = ExpressionHelper.eval(ctx);
        System.out.println(value);
        String[] values = value.split("_");
        Assert.assertEquals("ABCD", values[0]);
        Assert.assertEquals(17, values[1].length());
        Assert.assertEquals("00001", values[2]);
        Assert.assertEquals("00000002", values[3]);
        Assert.assertEquals(8, values[4].length());
        Assert.assertEquals("EFGH", values[5]);
    }
}