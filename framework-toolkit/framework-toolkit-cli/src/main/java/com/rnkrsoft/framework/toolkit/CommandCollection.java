package com.rnkrsoft.framework.toolkit;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devops4j on 2017/1/4.
 */
public class CommandCollection {
    @Getter
    final Map<String, CommandDefine> optionCollection = new HashMap();
    /**
     * 增加定义
     * @param define 定义
     */
    public void addDefine(CommandDefine define){
        optionCollection.put(define.getCmd(), define);
    }
}
