package com.rnkrsoft.framework.orm.log4jdbc.sql;

import org.junit.Test;

/**
 * Created by Administrator on 2018/7/8.
 */
public class UtilitiesTest {

    @Test
    public void testRightJustify() throws Exception {
        String value = Utilities.rightJustify(10, "A");
        System.out.println(value);
    }
}