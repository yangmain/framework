package com.rnkrsoft.framework.orm;

import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
public interface OrderBy {
    OrderBy addOrderBy(String column, Order order);
    OrderBy addOrderBy(OrderByColumn orderByColumn);
    /**
     * 获取排序字段
     * @return 排序字段名
     */
    List<OrderByColumn> getOrderByColumns();
}
