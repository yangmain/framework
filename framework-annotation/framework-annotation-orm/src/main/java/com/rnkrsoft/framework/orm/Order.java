package com.rnkrsoft.framework.orm;

import com.rnkrsoft.interfaces.EnumStringCode;

/**
 * Created by rnkrsofr.com on 2018/5/8.
 * 排序
 */
public enum Order implements EnumStringCode{
    ASC("asc", "升序排列"),
    DESC("desc", "降序排列");
    String code;
    String desc;
    Order(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
