package com.rnkrsoft.framework.orm.spring.dao;

import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;
import com.rnkrsoft.framework.orm.spring.entity.JpaEntity;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
public interface JpaDemoDAO extends JdbcMapper<JpaEntity, String> {
}
