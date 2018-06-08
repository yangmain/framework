package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.ValueMode;

/**
 * Created by rnkrsoft.com on 2018/6/5.
 */
public enum MongoValueMode {
    LT("$lt", "小于", ValueMode.LT),
    LTE("$lte", "小于", ValueMode.LTE),
    GT("$gt", "大于", ValueMode.GT),
    GTE("$lt", "小于", ValueMode.GTE),
    NE("$ne", "不等于", ValueMode.NE);
    String code;
    String desc;
    ValueMode valueMode;

    MongoValueMode(String code, String desc, ValueMode valueMode) {
        this.code = code;
        this.desc = desc;
        this.valueMode = valueMode;
    }

}
