package com.rnkrsoft.framework.orm.config;

import com.rnkrsoft.framework.orm.WordMode;

import java.util.Map;

/**
 * Created by rnkrsoft on 2018/4/2.
 */
public interface OrmConfig {
    /**
     * DAO所在的包路径，ORM启动时扫描
     * @return 包路径数组
     */
    String[] getDaoPackages();

    /**
     * 是否启动懒加载初始化，主要用于开发模式下
     * @return 是否进行懒加载
     */
    boolean isLazyInit();

    /**
     * 获取关键字模式
     * @return 关键字是否大小写
     */
    WordMode getKeywordMode();

    /**
     * 获取SQL模式
     * @return SQL是否大小写
     */
    WordMode getSqlMode();

    /**
     * 获取全局配置
     * @return 全局配置
     */
    ItemConfig getGlobalConfig();

    /**
     * 获取对每个具体包或者带掩码的包路径单独的配置
     * @return 包路径对应配置信息集合
     */
    Map<String, ItemConfig> getDaoConfigs();

    /**
     * 设置每个具体包或者带掩码的包路径配置信息
     * @param config 包路径对应配置信息集合
     */
    void setDaoConfigs(Map<String, ItemConfig> config);
}
