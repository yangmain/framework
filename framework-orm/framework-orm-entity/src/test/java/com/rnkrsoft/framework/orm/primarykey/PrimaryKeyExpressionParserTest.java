package com.rnkrsoft.framework.orm.primarykey;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by rnhrsoft.com on 2018/4/11.
 */
public class PrimaryKeyExpressionParserTest {

    @Test
    public void testParse() throws Exception {
        List<PrimaryKeyExpressionParser.Value> list = new PrimaryKeyExpressionParser().parse("ABCD${yyyyMMdd}${seqNo:5}EFGH");
        for (PrimaryKeyExpressionParser.Value value : list){
            System.out.println(value.getValue());
        }
    }


    @Test
    public void testParse1() throws Exception {

    }
}