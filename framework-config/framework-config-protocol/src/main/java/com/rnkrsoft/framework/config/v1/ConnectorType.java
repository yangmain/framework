package com.rnkrsoft.framework.config.v1;

import com.devops4j.interfaces.EnumStringCode;
import com.devops4j.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 连接器类型
 */
public enum ConnectorType implements EnumStringCode{
    HTTP("HTTP", "超文本协议"),
    DUBBO("DUBBO", "DUBBO协议");
    String code;
    String desc;

    ConnectorType(String code, String desc) {
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

    public static ConnectorType valueOfCode(String code){
        for (ConnectorType value : values()){
            if(value.code.equals(code)){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的工作模式 '{}'", code).runtimeException();
    }
}