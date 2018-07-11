package com.rnkrsoft.framework.orm.mongo.utils;

import com.rnkrsoft.framework.orm.mongo.entity.OperateLogEntity;
import com.rnkrsoft.framework.sequence.spring.ClasspathSequenceServiceConfigure;
import lombok.ToString;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/10.
 */
public class BeanUtilsTest {
    @ToString
    static class Demo implements Serializable{
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
    static class Demo1 implements Serializable{
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
    static class Demo2 implements Serializable{
        String title;

        public Demo2() {
        }

        public Demo2(String title) {
            this.title = title;
        }
    }

    @Test
    public void testDescribe() throws Exception {
        Demo demo = new Demo(null, 5);
        demo.address1 = new Demo1("xssss", 123456L);
        demo.address1.demo2 = new Demo2("dsxsxs");
        Map<String, Object> map = BeanUtils.describe(demo, BeanUtils.BeanSetting.builder().nullable(true).build());
        System.out.println(map);
        Demo demo11 = BeanUtils.populate(map, Demo.class,  BeanUtils.BeanSetting.builder().nullable(true).build());
        System.out.println(demo11);
    }

    @Test
    public void testMongoAnn() throws Exception {
        OperateLogEntity operateLogEntity = new OperateLogEntity();
        operateLogEntity.setName("this is a test");
        Map<String, Object> map = BeanUtils.describe(operateLogEntity, BeanUtils.BeanSetting.builder().nullable(true).sequenceServiceConfigure(new ClasspathSequenceServiceConfigure()).tableMetadata(MongoEntityUtils.extractTable(operateLogEntity.getClass())).build());
        System.out.println(map);
    }
}