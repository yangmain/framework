package com.rnkrsoft.framework.toolkit.cli;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2017/1/4.
 * 参数对象
 */
@Data
@NoArgsConstructor
public class Option {
    /**
     * 长名字
     */
    String longName;
    /**
     * 短名字
     */
    String shortName;
    /**
     * 是否必须
     */
    boolean require;
    /**
     * 参数数目
     */
    int argNum;
    /**
     * 描述
     */
    String desc;
    /**
     * 例子
     */
    String example;
    /**
     * 默认值
     */
    List<String> defaultValue;

    public Option(String longName, String shortName, boolean require, int argNum, String desc, String example,  List<String> defaultValue) {
        this.longName = longName;
        this.shortName = shortName;
        this.require = require;
        this.argNum = argNum;
        this.desc = desc;
        this.example = example;
        if (!defaultValue.isEmpty()) {
            this.defaultValue = new ArrayList();
            this.defaultValue.addAll(defaultValue);
        }
    }
}
