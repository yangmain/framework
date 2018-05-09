package com.rnkrsoft.framework.orm;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
@Data
@Builder
@ToString
public class OrderByColumn {
    /**
     * 字段名
     */
    String column;
    /**
     * 排序
     */
    Order order = Order.DESC;
}
