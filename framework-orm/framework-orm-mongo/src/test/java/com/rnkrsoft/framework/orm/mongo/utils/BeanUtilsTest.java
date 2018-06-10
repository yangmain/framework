package com.rnkrsoft.framework.orm.mongo.utils;

import lombok.ToString;
import org.junit.Test;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/10.
 */
public class BeanUtilsTest {
    @ToString
    static class Demo {
        String name;
        int age;
        Demo1 address1;

        public Demo() {
        }

        public Demo(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
    @ToString
    static class Demo1 {
        String address;
        long count;
        Demo2 demo2;

        public Demo1() {
        }

        public Demo1(String address, long count) {
            this.address = address;
            this.count = count;
        }
    }
    @ToString
    static class Demo2 {
        String title;

        public Demo2() {
        }

        public Demo2(String title) {
            this.title = title;
        }
    }

    @Test
    public void testDescribe() throws Exception {
        Demo demo = new Demo("xxx", 5);
        demo.address1 = new Demo1("xssss", 123456L);
        demo.address1.demo2 = new Demo2("dsxsxs");
        Map<String, Object> map = BeanUtils.describe(demo, null);
        System.out.println(map);
        Demo demo11 = BeanUtils.populate(map, Demo.class, null);
        System.out.println(demo11);
    }

    @Test
    public void testPopulate() throws Exception {

    }
}