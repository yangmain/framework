package com.rnkrsoft.framework.config.v1;

import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.message.MessageFormatter;

/**
 * Created by rnkrsoft.com on 2017/2/8.
 * 返回状态
 */
public enum ResponseCode implements EnumStringCode {
    SUCCESS("0000", "成功"),
    NO_DATA("0001", "暂无数据"),
    HAPPENS_ERROR("0002", "程序异常"),
    NOT_AUTHORITY("0003", "没有权限"),
    ILLEGAL_INSTRUCTION("0004", "无效指令"),
    FAIL("9999", "失败");

    String code;
    String desc;

    ResponseCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    public static ResponseCode valueOfCode(String code){
        ResponseCode[] values = values();
        for (ResponseCode value : values){
            if(value.code.equals(code)){
                return value;
            }
        }
        throw new IllegalArgumentException(MessageFormatter.format("无效的编码 '{}'", code));
    }

}