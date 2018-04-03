package com.rnkrsoft.framework.test.config;

import com.rnkrsoft.framework.orm.WordMode;
import com.rnkrsoft.framework.orm.config.ItemConfig;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
public class TestOrmConfig implements OrmConfig {
    /**
     * DAO所在的包路径，MyBatis启动时扫描
     */
    @Setter
    String[] daoPackages;
    /**
     * 启动懒加载初始化，主要用于开发模式下
     */
    boolean lazyInit = true;
    /**
     * 获取关键字模式
     */
    @Setter
    WordMode keywordMode;
    /**
     * 获取SQL模式
     */
    @Setter
    WordMode sqlMode;
    /**
     * 全局配置
     */
    @Setter
    ItemConfig globalConfig;
    /**
     * 明细配置
     */
    @Setter
    Map<String, ItemConfig> daoConfigs;
}
