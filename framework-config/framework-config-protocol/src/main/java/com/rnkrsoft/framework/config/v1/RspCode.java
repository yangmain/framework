package com.rnkrsoft.framework.config.v1;

import com.devops4j.interfaces.EnumStringCode;
import com.devops4j.message.MessageFormatter;

/**
 * Created by liyue on 2017/2/8.
 * 返回状态
 */
public enum RspCode implements EnumStringCode {
    SUCCESS("0000", "成功"),
    NO_DATA("0001", "暂无数据"),
    HAPPENS_ERROR("0002", "程序异常"),
    NOT_AUTHORITY("0003", "没有权限"),
    ILLEGAL_INSTRUCTION("0004", "无效指令"),
    FAIL("9999", "失败");

    String code;
    String desc;

    RspCode(String code, String desc) {
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
    public static RspCode valueOfCode(String code){
        RspCode[] values = values();
        for (RspCode value : values){
            if(value.code.equals(code)){
                return value;
            }
        }
        throw new IllegalArgumentException(MessageFormatter.format("无效的编码 '{}'", code));
    }

}