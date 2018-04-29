package com.rnkrsoft.framework.test.service;

/**
 * Created by woate on 2018/4/26.
 */
public interface UserService {
    /**
     * 注册用户
     * @param name 名字
     * @param age 年龄
     * @return 用户号
     */
    String register(String name, int age);
}
