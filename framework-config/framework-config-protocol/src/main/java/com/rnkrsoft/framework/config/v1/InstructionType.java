package com.rnkrsoft.framework.config.v1;

import com.devops4j.interfaces.EnumIntegerCode;
import com.devops4j.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
public enum InstructionType implements EnumIntegerCode{
    FETCH(1, "拉取配置"),
    PUSH(2, "推送配置"),
    PUSH_READONLY_SYNC(3, "同步只读推送"),
    PUSH_READONLY_ASYN(4, "异步只读推送"),
    HEARTBEAT(5, "心跳检测"),
    CLIENT_INFO(6, "获取客户端信息"),
    SUPPORT_INSTRUCTION_SET(7, "获取支持的指令集列表");
    int code;
    String desc;

    InstructionType(int code, String desc) {
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

    public static InstructionType valueOfCode(int code){
        for (InstructionType value : values()){
            if(value.code == code){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的指令代码 '{}'", code).runtimeException();
    }
}
