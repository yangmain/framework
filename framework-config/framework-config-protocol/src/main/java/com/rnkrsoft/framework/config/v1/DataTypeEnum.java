package com.rnkrsoft.framework.config.v1;

import com.rnkrsoft.interfaces.EnumIntegerCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/17.
 */
public enum DataTypeEnum implements EnumIntegerCode {
    BOOLEAN(1, "布尔值"),
    INTEGER(2, "整数"),
    STRING(3, "字符串");

    DataTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    int code;
    String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DataTypeEnum valueOfCode(int code) {
        for (DataTypeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw ErrorContextFactory.instance()
                .message("编码'{}'无效", code)
                .runtimeException();
    }
}
