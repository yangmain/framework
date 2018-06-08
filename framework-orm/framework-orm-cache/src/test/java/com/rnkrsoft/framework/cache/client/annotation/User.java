package com.rnkrsoft.framework.cache.client.annotation;

import lombok.ToString;

/**
 * Created by woate on 2018/6/2.
 */
@ToString
public class User {
    String name;
    int age;
    Boolean sex;

    public User(String name, int age, Boolean sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
