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

    @Setter
    String[] mapperLocations;
    /**
     * 是否允许重复加载
     */
    boolean allowReload = true;
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
    ItemConfig global;
    /**
     * 明细配置
     */
    @Setter
    Map<String, ItemConfig> configs;
    @Setter
    Map<String, String> sequenceMappings;
}
