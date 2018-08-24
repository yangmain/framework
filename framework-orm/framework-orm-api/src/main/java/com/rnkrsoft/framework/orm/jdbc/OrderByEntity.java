package com.rnkrsoft.framework.orm.jdbc;

import com.rnkrsoft.framework.orm.*;
import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * Created by rnkrsoft.com on 2018/8/6.
 */
@Data
@ToString
public abstract class OrderByEntity implements OrderBy, ValueBy {
    @Ignore
    final List<OrderByColumn> orderByColumns = new ArrayList();
    @Ignore
    final Map<String, ValueByColumn> valueModes = new HashMap();

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

    @Override
    public ValueBy addValueBy(String column, ValueMode valueMode) {
        valueModes.put(column, null);
        return this;
    }

    @Override
    public ValueBy addValueBy(String[] columns, ValueMode valueMode) {
        for (String column : columns){
            addValueBy(column, valueMode);
        }
        return this;
    }

    @Override
    public ValueBy addValueBy(ValueByColumn... valueByColumns) {
        for (ValueByColumn valueByColumn : valueByColumns){
            valueModes.put(valueByColumn.getColumn(), valueByColumn);
        }
        return this;
    }

    @Override
    public Map<String, ValueByColumn> getValueByMap() {
        return Collections.unmodifiableMap(valueModes);
    }
}
