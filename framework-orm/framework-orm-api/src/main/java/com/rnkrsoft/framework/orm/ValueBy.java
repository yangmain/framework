package com.rnkrsoft.framework.orm;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/6/3.
 */
public interface ValueBy {
    /**
     * 增加字段模式
     * @param column
     * @param valueMode
     * @return
     */
    ValueBy addValueBy(String column, ValueMode valueMode);

    /**
     * 增加多个字段模式
     * @param columns
     * @param valueMode
     * @return
     */
    ValueBy addValueBy(String[] columns, ValueMode valueMode);

    /**
     * 增加多个字段模式
     * @param columns
     * @return
     */
    ValueBy addValueBy(ValueByColumn ... columns);

    /**
     * 获取值模式列表
     * @return
     */
    Map<String, ValueByColumn> getValueByMap();
}
