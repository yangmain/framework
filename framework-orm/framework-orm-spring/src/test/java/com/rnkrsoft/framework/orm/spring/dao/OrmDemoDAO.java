package com.rnkrsoft.framework.orm.spring.dao;

import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;
import com.rnkrsoft.framework.orm.spring.entity.OrmEntity;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
public interface OrmDemoDAO extends JdbcMapper<OrmEntity, String> {
}
