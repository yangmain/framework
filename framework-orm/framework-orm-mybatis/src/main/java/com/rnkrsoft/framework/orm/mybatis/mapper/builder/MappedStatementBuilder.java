package com.rnkrsoft.framework.orm.mybatis.mapper.builder;

import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.config.ItemConfig;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.Arrays;

/**
 * Created by devops4j on 2016/12/18.
 */
@ToString
@Data
public abstract class MappedStatementBuilder {
    public MappedStatementBuilder(Configuration config, OrmConfig ormConfig, String namespace, Class mapperClass, Class entityClass, Class keyClass) {
        this.config = config;
        this.ormConfig = ormConfig;
        this.namespace = namespace;
        this.mapperClass = mapperClass;
        this.entityClass = entityClass;
        this.keyClass = keyClass;
        EntityExtractorHelper helper = new EntityExtractorHelper();
        this.tableMetadata = helper.extractTable(entityClass, ormConfig.isStrict());
    }

    private TableMetadata tableMetadata;

    OrmConfig ormConfig;
    /**
     * 配置对象
     */
    protected Configuration config;
    /**
     * 命名空间
     */
    protected String namespace;

    /**
     * Mapper类对象
     */
    protected Class mapperClass;
    /**
     * 实体类对象
     */
    protected Class entityClass;
    /**
     * 主键类对象
     */
    protected Class keyClass;

    public TableMetadata getTableMetadata() {
        ItemConfig itemConfig = ormConfig.get(mapperClass.getName());
        if (itemConfig.getSchemaMode() != NameMode.entity) {
            this.tableMetadata.setSchema(itemConfig.getSchema());
        }
        if (itemConfig.getPrefixMode() != NameMode.entity) {
            this.tableMetadata.setPrefix(itemConfig.getPrefix());
        }
        if (itemConfig.getSuffixMode() != NameMode.entity) {
            this.tableMetadata.setSuffix(itemConfig.getSuffix());
        }
        return this.tableMetadata;
    }

    /**
     * 将多个节点组装成组合节点
     *
     * @param sqlNodes 节点数组
     * @return 组合节点
     */
    protected MixedSqlNode mixedContents(SqlNode... sqlNodes) {
        return new MixedSqlNode(Arrays.asList(sqlNodes));
    }

    /**
     * 构建MapperStatement
     *
     * @return MapperStatement
     */
    public abstract MappedStatement build();
}
