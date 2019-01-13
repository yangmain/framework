package com.rnkrsoft.framework.orm;

import lombok.Data;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
@Data
@ToString
public class ValueByColumn {
    /**
     * 字段名
     */
    String column;
    /**
     * 值模式
     */
    ValueMode valueMode;
}
