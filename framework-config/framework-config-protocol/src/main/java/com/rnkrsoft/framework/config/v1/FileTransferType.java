package com.rnkrsoft.framework.config.v1;

import com.devops4j.interfaces.EnumIntegerCode;
import com.devops4j.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 传输类型
 */
public enum FileTransferType implements EnumIntegerCode{
    SOCKET(1, "套接字传输"),
    HTTP(2, "超文本协议传输"),
    FTP(3, "文件传输协议传输");
    int code;
    String desc;

    FileTransferType(int code, String desc) {
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

    public static FileTransferType valueOfCode(int code){
        for (FileTransferType value : values()){
            if(value.code == code){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效的传输类型 '{}'", code).runtimeException();
    }
}
