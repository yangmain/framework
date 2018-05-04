package com.rnkrsoft.framework.orm.config;

import com.rnkrsoft.framework.orm.DatabaseType;
import com.rnkrsoft.framework.orm.NameMode;
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
     * 是否是严格模式
     */
    boolean strict;
    /**
     * 数据库类型
     */
    DatabaseType databaseType = DatabaseType.MySQL;
    /**
     * DAO所在的包路径
     */
    String[] daoPackages;
    /**
     * MAPPER文件路径
     */
    String[] mapperLocations;
    /**
     * 是否允许重建表结构,正式发布不允许重建表结构
     */
    final boolean allowReload = false;
    /**
     * 获取关键字模式
     */
    WordMode keywordMode = WordMode.upperCase;
    /**
     * 获取SQL模式
     */
    WordMode sqlMode = WordMode.upperCase;
    /**
     * 全局配置
     */
    ItemConfig global;
    /**
     * 明细配置
     */
    Map<String, ItemConfig> configs;

    Map<String, String> sequenceMappings;

    @Override
    public ItemConfig get(String daoInterface) {
        ItemConfig newItemConfig = new ItemConfig();
        NameMode schemaMode = getGlobal().getSchemaMode();
        String schema = getGlobal().getSchema();
        NameMode prefixMode = getGlobal().getPrefixMode();
        String prefix = getGlobal().getPrefix();
        NameMode suffixMode = getGlobal().getSuffixMode();
        String suffix = getGlobal().getSuffix();
        ItemConfig itemConfig = getConfigs().get(daoInterface);

        if (itemConfig != null) {
            schemaMode = itemConfig.getSchemaMode();
            schema = itemConfig.getSchema();
            prefixMode = itemConfig.getPrefixMode();
            prefix = itemConfig.getPrefix();
            suffixMode = itemConfig.getSuffixMode();
            suffix = itemConfig.getSuffix();
        }
        newItemConfig.setSchemaMode(schemaMode);
        newItemConfig.setSchema(schema);
        newItemConfig.setPrefixMode(prefixMode);
        newItemConfig.setPrefix(prefix);
        newItemConfig.setSuffixMode(suffixMode);
        newItemConfig.setSuffix(suffix);
        return newItemConfig;
    }
}
