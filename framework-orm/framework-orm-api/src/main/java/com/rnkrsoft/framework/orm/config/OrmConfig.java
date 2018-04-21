package com.rnkrsoft.framework.orm.config;

import com.rnkrsoft.framework.orm.DatabaseType;
import com.rnkrsoft.framework.orm.NameMode;
import com.rnkrsoft.framework.orm.WordMode;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 */
public interface OrmConfig {
    /**
     * 严格注解模式
     */
    boolean isStrict();
    /**
     * 数据库类型
     */
    DatabaseType getDatabaseType();
    /**
     * DAO所在的包路径，ORM启动时扫描
     * @return 包路径数组
     */
    String[] getDaoPackages();

    /**
     * 获取Mapper文件路径
     * @return Mapper文件路径
     */
    String[] getMapperLocations();

    /**
     * 是否允许重复加载，通常用于开发模式
     * @return 是否允许重复加载
     */
    boolean isAllowReload();

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
    ItemConfig getGlobal();

    /**
     * 获取对每个具体包或者带掩码的包路径单独的配置
     * @return 包路径对应配置信息集合
     */
    Map<String, ItemConfig> getConfigs();

    /**
     * 设置每个具体包或者带掩码的包路径配置信息
     * @param config 包路径对应配置信息集合
     */
    void setConfigs(Map<String, ItemConfig> config);

    /**
     * 配置序号服务信息中的表所用的序号服务实现类
     * @param sequenceMappings 序号服务配置
     */
    void setSequenceMappings(Map<String, String> sequenceMappings);

    /**
     * 获取序号服务配置信息
     * @return 表-生成规则
     */
    Map<String, String> getSequenceMappings();
}
