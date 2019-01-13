package com.rnkrsoft.framework.orm;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
@Data
@ToString
public class OrderByColumn {
    /**
     * 字段名
     */
    String column;
    /**
     * 排序
     */
    Order order = Order.ASC;

    public OrderByColumn(String column, Order order) {
        this.column = column;
        this.order = order;
    }

    public static class OrderByColumnBuilder{
        /**
         * 字段名
         */
        String column;
        /**
         * 排序
         */
        Order order = Order.ASC;

        private OrderByColumnBuilder(String column, Order order) {
            this.column = column;
            this.order = order;
        }

        private OrderByColumnBuilder(String column) {
            this.column = column;
        }

        public OrderByColumn build(){
            return new OrderByColumn(this.column, this.order);
        }

        public OrderByColumnBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public OrderByColumnBuilder column(String column) {
            this.column = column;
            return this;
        }
    }

    public static OrderByColumnBuilder builder(String column, Order order){
        return new OrderByColumnBuilder(column, order);
    }

    public static OrderByColumnBuilder builder(String column){
        return new OrderByColumnBuilder(column);
    }
}
