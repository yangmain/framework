package com.rnkrsoft.framework.config.v1;

import com.devops4j.interfaces.EnumStringCode;
import com.devops4j.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 运行模式
 */
public enum RuntimeMode implements EnumStringCode{
    AUTO("AUTO", "自动识别，优先远程，远程失败后使用本地"),
    LOCAL("LOCAL", "本地模式"),
    REMOTE("REMOTE","远程模式");
    String code;
    String desc;

    RuntimeMode(String code, String desc) {
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

    public static RuntimeMode valueOfCode(String code){
        for (RuntimeMode value : values()){
            if(value.code.equals(code)){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的运行模式 '{}'", code).runtimeException();
    }
}
