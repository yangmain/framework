package com.rnkrsoft.framework.config.v1;

import com.rnkrsoft.interfaces.EnumIntegerCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
public enum ParamType implements EnumIntegerCode{
    SYSTEM(1, "系统参数"),
    APPLICATION(2, "应用参数"),
    USER(3, "用户参数");
    int code;
    String desc;

    ParamType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static ParamType valueOfCode(int code){
        for (ParamType value : values()){
            if(value.code == code){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的参数类型 '{}'", code).runtimeException();
    }
}
