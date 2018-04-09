package com.rnkrsoft.framework.orm.config;

import com.rnkrsoft.framework.orm.WordMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 * 基于ORM配置对象的正式实现
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseOrmConfig implements OrmConfig{
    /**
     * DAO所在的包路径
     */
    String[] daoPackages;
    /**
     * MAPPER文件路径
     */
    String[] mapperLocations;
    /**
     * 是否允许重复加载
     */
    boolean allowReload = false;
    /**
     * 获取关键字模式
     */
    WordMode keywordMode;
    /**
     * 获取SQL模式
     */
    WordMode sqlMode;
    /**
     * 全局配置
     */
    ItemConfig global;
    /**
     * 明细配置
     */
    Map<String, ItemConfig> configs;

    Map<String, String> sequenceMappings;
}
