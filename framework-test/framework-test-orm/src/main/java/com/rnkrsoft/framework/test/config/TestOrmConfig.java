package com.rnkrsoft.framework.test.config;

import com.rnkrsoft.framework.orm.config.ItemConfig;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.jdbc.DatabaseType;
import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.Properties;

@Getter
public class TestOrmConfig implements OrmConfig {
    boolean strict;
    /**
     * 数据库类型
     */
    DatabaseType databaseType = DatabaseType.MySQL;
    /**
     * DAO所在的包路径，MyBatis启动时扫描
     */
    @Setter
    String[] daoPackages;

    @Setter
    Resource[] mapperLocations;
    /**
     * 是否允许重载
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
    Properties sequenceMappings;

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
