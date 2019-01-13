package com.rnkrsoft.framework.config.spring.autoconfig;

import com.rnkrsoft.framework.config.spring.autoconfig.annotation.EnableDynamicConfig;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/9.
 */
@EnableDynamicConfig(groupId = "com.rnkrsoft", artifactId = "test-model")
public class DynamicConfigRegistrarTest {

    @Test
    public void testRegisterBeanDefinitions() throws Exception {

    }
}