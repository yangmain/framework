package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.interfaces.EnumStringCode;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/6/5.
 */
public enum MongoLogicMode implements EnumStringCode{
    AND("", "且", LogicMode.AND),
    OR("$or", "或", LogicMode.OR);
    String code;
    String desc;
    LogicMode logicMode;

    MongoLogicMode(String code, String desc, LogicMode logicMode) {
        this.code = code;
        this.desc = desc;
        this.logicMode = logicMode;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static MongoLogicMode valueOfCode(LogicMode logicMode){
        for (MongoLogicMode value : values()){
            if (value.logicMode == logicMode){
                return value;
            }
        }
        throw ErrorContextFactory.instance().message("无效逻辑模式'{}'", logicMode).runtimeException();
    }
}
