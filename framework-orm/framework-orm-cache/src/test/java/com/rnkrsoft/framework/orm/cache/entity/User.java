package com.rnkrsoft.framework.orm.cache.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
@Data
@ToString
public class User implements Serializable{
    String name;
    int age;
    Boolean sex;

    public User(String name, int age, Boolean sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
