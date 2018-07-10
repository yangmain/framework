package com.rnkrsoft.framework.orm.mongo.spring;

import com.rnkrsoft.framework.test.SpringTest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/7/9.
 */
@ContextConfiguration("classpath*:testContext-mongo.xml")
public class MongoScannerConfigurerTest extends SpringTest{

    @Test
    public void testAfterPropertiesSet() throws Exception {
        
    }
}