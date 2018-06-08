package com.rnkrsoft.framework.orm.jdbc.select;

import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.jdbc.JdbcInterface;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface SelectPageOrMapper<T,K> extends JdbcInterface {
    /**
     * 按照分页进行查找记录
     * @param pagination 分页实体，提供分页机制，属性params提供参数的传入
     * @return 分页实体
     */
    Pagination<T> selectPageOr(Pagination<T> pagination);
}
