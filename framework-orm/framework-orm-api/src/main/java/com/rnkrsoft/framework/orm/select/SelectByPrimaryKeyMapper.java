package com.rnkrsoft.framework.orm.select;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface SelectByPrimaryKeyMapper<T,K> {
    /**
     * 根据物理主键查询
     * @param pk
     * @return
     */
    T selectByPrimaryKey(K pk);
}
