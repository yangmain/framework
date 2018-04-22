package com.rnkrsoft.framework.orm.mybatis.mapper.builder.insert;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.sequence.PrimaryKeyHelper;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import com.rnkrsoft.framework.orm.mybatis.sequence.SequenceServiceConfigure;


import java.util.*;

/**
 * Created by devops4j on 2016/12/18.
 * 全字段插入MS建造器
 */
@Slf4j
public class InsertMappedStatementBuilder extends MappedStatementBuilder {
    /**
     * 序号服务配置对象
     */
    protected SequenceServiceConfigure sequenceConfigure;

    public InsertMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass, SequenceServiceConfigure sequenceConfigure) {
        super(config, ormConfig, mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, SelectMapper.class));
        this.sequenceConfigure = sequenceConfigure;
    }

    @Override
    public MappedStatement build() {
        String tableName = KeywordsUtils.convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode());
        System.out.println("===================================================================" + tableName);
        TextSqlNode insertIntoSqlNode = new TextSqlNode(KeywordsUtils.convert("INSERT INTO ", getOrmConfig().getKeywordMode()) + tableName  + "(");
        List<SqlNode> heads = new ArrayList();
        List<SqlNode> values = new ArrayList();
        Map<String, ColumnMetadata> fields = getTableMetadata().getColumnMetadataSet();
        for (String column : getTableMetadata().getOrderColumns()) {
            ColumnMetadata columnMetadata = fields.get(column);
            heads.add(new TextSqlNode(KeywordsUtils.convert(columnMetadata.getJdbcName(), getOrmConfig().getSqlMode()) + ","));
            String valueExp = "#{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " },";
            values.add(new TextSqlNode(valueExp));
        }
        MixedSqlNode headsSqlNode = new MixedSqlNode(heads);
        MixedSqlNode valuesSqlNode = new MixedSqlNode(values);
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(insertIntoSqlNode
                , new TrimSqlNode(config, headsSqlNode, "", "", "", ",")//用于去除多余的逗号
                , new TextSqlNode(") ")
                , new TextSqlNode(KeywordsUtils.convert("VALUES", getOrmConfig().getKeywordMode()))
                , new TextSqlNode(" (")
                , new TrimSqlNode(config, valuesSqlNode, "", "", "", ",")//用于去除多余的逗号
                , new TextSqlNode(")")));

        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.INSERT, sqlSource, SqlCommandType.INSERT);
        msBuilder = msBuilder.flushCacheRequired(true).useCache(false);
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType()));
            parameterMappings.add(builder.build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "BaseParameterMap", entityClass, parameterMappings);
        List<String> primaryKeys = getTableMetadata().getPrimaryKeys();
        //存在主键，肯定的
        if (!primaryKeys.isEmpty()) {
            final String pkColumnName = primaryKeys.get(0);
            final ColumnMetadata primaryKeyMetadata = fields.get(pkColumnName);
            PrimaryKeyHelper.generate(sequenceConfigure, getTableMetadata(), primaryKeyMetadata, msBuilder);
        }
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
