package com.rnkrsoft.framework.orm;

import com.rnkrsoft.interfaces.EnumStringCode;

/**
 * Created by liucheng on 2018/6/22.
 * 支持的数据类型
 */
public enum SupportedDataType implements EnumStringCode {
    CHAR("CHAR", "固定字符串"),
    BIGINT("BIGINT", "长整"),
    VARCHAR("VARCHAR", "可变字符串"),
    SMALLINT("SMALLINT", "短整型"),
    DATETIME("DATETIME", "日期时间"),
    TEXT("TEXT", "长字符串文本"),
    DOUBLE("DOUBLE", "浮点数"),
    INT("INT", "整型"),
    TINYINT("TINYINT", "字节"),
    TIMESTAMP("TIMESTAMP", "时间戳"),
    DECIMAL("DECIMAL", "浮点数"),
    DATE("DATE", "日期类型"),
    LONGTEXT("LONGTEXT", "超长字符串文本");
    String code;
    String desc;

    SupportedDataType(String code, String desc) {
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

    public static SupportedDataType valueOfCode(String code){
        for (SupportedDataType value : values()){
            if (value.code.equals(code)){
                return value;
            }
        }
        return VARCHAR;
    }
}
