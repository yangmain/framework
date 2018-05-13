package com.rnkrsoft.framework.orm.mybatis.sequence;


import com.rnkrsoft.framework.sequence.SequenceService;

/**
 * Created by rnkrsoft.com on 2017/1/8.
 * 序号服务配置接口
 */
public interface SequenceServiceConfigure {
    /**
     * 根据表名获取序号服务
     * @param tableName 表名
     * @return 序号服务
     */
    SequenceService getSequenceService(String tableName);

    /**
     * 注册序号服务的到指定的表
     * @param tableName 表名
     * @param clazz 服务类实现
     * @return
     */
    SequenceService register(String tableName, String clazz);
}
