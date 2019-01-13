package com.rnkrsoft.framework.test;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/11.
 */
public class EnvironmentUtilsTest {

    @Test
    public void testDetermineRuntime() throws Exception {
        EnvironmentUtils.Environment environment = EnvironmentUtils.determineRuntime();
        System.out.println(environment);
    }
}