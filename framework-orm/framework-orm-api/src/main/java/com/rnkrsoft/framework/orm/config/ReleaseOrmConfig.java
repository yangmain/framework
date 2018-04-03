package com.rnkrsoft.framework.orm.config;

import com.rnkrsoft.framework.orm.WordMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created by rnkrsoft on 2018/4/2.
 * 基于ORM配置对象的正式实现
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseOrmConfig implements OrmConfig{
    /**
     * DAO所在的包路径，MyBatis启动时扫描
     */
    String[] daoPackages;
    /**
     * 是否启动懒加载初始化，主要用于开发模式下
     */
    boolean lazyInit = false;
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
    ItemConfig globalConfig;
    /**
     * 明细配置
     */
    Map<String, ItemConfig> daoConfigs;
}
