package com.rnkrsoft.framework.orm.lock;

/**
 * Created by devops4j on 2016/12/18.
 */
public interface LockByUpdateSetPrimaryKeyMapper<T,K> {
    /**
     * 通过更新主键锁定记录
     * @param pk
     * @return
     */
    int lockByUpdateSetPrimaryKey(K pk);
}
