package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/5.
 */
public enum MongoValueMode implements EnumStringCode{
    LT("$lt", "小于", ValueMode.LT),
    LTE("$lte", "小于", ValueMode.LTE),
    GT("$gt", "大于", ValueMode.GT),
    GTE("$lt", "小于", ValueMode.GTE),
    NE("$ne", "不等于", ValueMode.NE),
    NONE("","无", ValueMode.NONE);
    String code;
    String desc;
    ValueMode valueMode;

    MongoValueMode(String code, String desc, ValueMode valueMode) {
        this.code = code;
        this.desc = desc;
        this.valueMode = valueMode;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static MongoValueMode valueOfCode(ValueMode valueMode){
        for (MongoValueMode value : values()){
            if (value.valueMode == valueMode){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效值模式'{}'", valueMode).runtimeException();
    }
}
