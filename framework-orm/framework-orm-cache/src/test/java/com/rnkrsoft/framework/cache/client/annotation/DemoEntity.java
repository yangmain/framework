package com.rnkrsoft.framework.cache.client.annotation;

import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@ToString
public class DemoEntity {
    String name;
    int age;
    Boolean sex;

    public DemoEntity(String name, int age, Boolean sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
