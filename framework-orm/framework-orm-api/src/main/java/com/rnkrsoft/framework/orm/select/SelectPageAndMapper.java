package com.rnkrsoft.framework.orm.select;

import com.rnkrsoft.framework.orm.Pagination;

/**
 * Created by rnkrsoft.com on 2016/12/18.
 */
public interface SelectPageAndMapper<T,K> {
    /**
     * 按照分页进行查找记录
     * @param pagination 分页实体，提供分页机制，属性params提供参数的传入
     * @return 分页实体
     */
    Pagination<T> selectPageAnd(Pagination<T> pagination);
}
