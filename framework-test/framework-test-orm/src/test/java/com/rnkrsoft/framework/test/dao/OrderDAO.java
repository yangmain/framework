package com.rnkrsoft.framework.test.dao;

import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;
import com.rnkrsoft.framework.test.entity.OrderEntity;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
public interface OrderDAO extends JdbcMapper<OrderEntity, String> {
}
