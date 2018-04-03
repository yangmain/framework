package com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.extractor.GenericityExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.untils.EntityExtractorUtils;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.WhereSqlNode;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by devops4j on 2016/12/18.
 */
public class LockByForUpdateOrMappedStatementBuilder extends MappedStatementBuilder {
    public LockByForUpdateOrMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, GenericityExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericityExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        TableMetadata tableMetadata = EntityExtractorUtils.extractTable(entityClass, strictWing4j);
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        String select = KeywordsUtils.convert("SELECT", keywordMode);
        String from = KeywordsUtils.convert("FROM", keywordMode);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.genreateSqlHead(entityClass, keywordMode, sqlMode, false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(KeywordsUtils.convert(tableMetadata.getTableName(), sqlMode)).append(" ");
        //创建结果映射
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        List<SqlNode> wheres = new ArrayList<SqlNode>();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            String whereSql = KeywordsUtils.convert(" OR ", keywordMode) + KeywordsUtils.convert(columnMetadata.getJdbcName(), sqlMode) + " = #{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            SqlNode node = new IfSqlNode(new TextSqlNode(whereSql), MessageFormat.format("{0} != null", columnMetadata.getJavaName()));
            wheres.add(node);
            parameterMappings.add(new ParameterMapping.Builder(config, columnMetadata.getJavaName(), registry.getTypeHandler(keyClass)).build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", entityClass, parameterMappings);
        SqlNode whereSqlNode = new WhereSqlNode(config, new MixedSqlNode(wheres), keywordMode);
        SqlNode forUpdateSqlNode = new StaticTextSqlNode(KeywordsUtils.convert(" FOR UPDATE", keywordMode));
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString()), whereSqlNode, forUpdateSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.LOCK_BY_FOR_UPDATE_ORD, sqlSource, SqlCommandType.SELECT);
        //创建参数映射
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ResultMapping.Builder builder = new ResultMapping.Builder(config, columnMetadata.getJavaName(), columnMetadata.getJdbcName(), columnMetadata.getJavaType());
            resultMappings.add(builder.build());
        }
        final ResultMap resultMap = new ResultMap.Builder(config, "BaseResultMap", entityClass, resultMappings).build();
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(resultMap);
        msBuilder.resultMaps(resultMaps);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
