package com.rnkrsoft.framework.orm;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体基类
 */
@Data
@ToString
public abstract class Entity implements OrderBy{
    @Ignore
    final List<OrderByColumn> orderByColumns = new ArrayList();

    @Override
    public OrderBy addOrderBy(String column, Order order) {
        return addOrderBy(OrderByColumn.builder(column).order(order).build());
    }

    @Override
    public OrderBy addOrderBy(OrderByColumn orderByColumn) {
        orderByColumns.add(orderByColumn);
        return this;
    }

    @Override
    public List<OrderByColumn> getOrderByColumns() {
        return orderByColumns;
    }
}
