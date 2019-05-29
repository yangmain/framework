package com.rnkrsoft.framework.config.v1;

import com.rnkrsoft.interfaces.EnumIntegerCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
public enum InstructionType implements EnumIntegerCode{
    FETCH(1, "拉取配置"),
    PUSH(2, "推送配置"),
    PUSH_READONLY_SYNC(3, "同步只读推送"),
    PUSH_READONLY_ASYN(4, "异步只读推送"),
    CLIENT_INFO(6, "获取客户端信息"),
    SUPPORT_INSTRUCTION_SET(7, "获取支持的指令集列表");
    int code;
    String desc;

    InstructionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static InstructionType valueOfCode(int code){
        for (InstructionType value : values()){
            if(value.code == code){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的指令代码 '{}'", code).runtimeException();
    }
}
