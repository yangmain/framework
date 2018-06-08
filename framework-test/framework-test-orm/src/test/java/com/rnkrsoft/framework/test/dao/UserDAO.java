package com.rnkrsoft.framework.test.dao;

import com.rnkrsoft.framework.orm.jdbc.JdbcMapper;
import com.rnkrsoft.framework.test.entity.UserEntity;

/**
 * Created by rnkrsoft.com on 2018/4/5.
 */
public interface UserDAO extends JdbcMapper<UserEntity, String> {
}
