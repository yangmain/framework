package com.rnkrsoft.framework.config.spring;

import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.framework.config.spring.aspect.DynamicConfigAspectJ;
import com.rnkrsoft.framework.config.spring.config.DemoConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

/**
 * Created by rnkrsoft.com on 2018/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:DynamicConfigSourceConfigurerTest.xml")
public class DynamicConfigSourceConfigurerTest {
    @Autowired
    DemoConfig demoConfig;

//    @Autowired
//    Demo demo;
    @Test
    public void testOpenFile() throws Exception {
//        System.out.println(demo);
        for (int i = 0; i < 100; i++) {
//            try {
//                InputStream is = demoConfig.openTest();
//                byte[] bytes = new byte[is.available()];
//                is.read(bytes);
//                System.out.println("openTest:" + new String(bytes));
//            } catch (Exception e) {
//                e.getCause().printStackTrace();
//            }
            System.out.println("param1:" + demoConfig.getParam1());
            System.out.println("param2:" + demoConfig.getParam2());
            System.out.println("param3:" + demoConfig.getParam3());
            System.out.println("param4:" + demoConfig.isParam4());
            System.out.println("log:" + demoConfig.getLog());
            Thread.sleep(1000);
        }
    }
}