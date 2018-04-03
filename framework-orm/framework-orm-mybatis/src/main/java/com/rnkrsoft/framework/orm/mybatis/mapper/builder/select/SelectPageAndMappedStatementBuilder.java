package com.rnkrsoft.framework.orm.mybatis.mapper.builder.select;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.extractor.GenericityExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.untils.EntityExtractorUtils;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.WhereSqlNode;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rnkrsoft.framework.orm.untils.KeywordsUtils.convert;


@Slf4j
public class SelectPageAndMappedStatementBuilder extends MappedStatementBuilder {

    public SelectPageAndMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, GenericityExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericityExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        TableMetadata tableMetadata = EntityExtractorUtils.extractTable(entityClass, strictWing4j);
        String select = convert("SELECT", keywordMode);
        String from = convert("FROM", keywordMode);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.genreateSqlHead(entityClass, keywordMode, sqlMode, false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(convert(tableMetadata.getTableName(), sqlMode)).append(" ");
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        List<SqlNode> wheres = new ArrayList<SqlNode>();
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            String whereSql = convert(" AND ", keywordMode) + convert(columnMetadata.getJdbcName(), sqlMode) + " = #{entity." + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            SqlNode node = new IfSqlNode(new TextSqlNode(whereSql), MessageFormat.format("entity.{0} != null", columnMetadata.getJavaName()));
            wheres.add(node);
            parameterMappings.add(new ParameterMapping.Builder(config, columnMetadata.getJavaName(), registry.getTypeHandler(keyClass)).build());
        }
        SqlNode whereSqlNode = new WhereSqlNode(config, new MixedSqlNode(wheres), keywordMode);
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString()), whereSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.SELECT_PAGE_AND, sqlSource, SqlCommandType.SELECT);
        msBuilder = msBuilder.flushCacheRequired(true).useCache(false);
        //创建参数映射
        for (String column: fields.keySet()){
            ColumnMetadata columnMetadata = fields.get(column);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, "entity."+ columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType()));
            parameterMappings.add(builder.build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "PageParameterMap", entityClass, parameterMappings);
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        for (String column: fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ResultMapping.Builder builder = new ResultMapping.Builder(config, columnMetadata.getJavaName(), convert(columnMetadata.getJdbcName(), sqlMode), columnMetadata.getJavaType());
            resultMappings.add(builder.build());
        }
        final ResultMap resultMap = new ResultMap.Builder(config, "PageBaseResultMap", entityClass, resultMappings).build();
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(resultMap);
        msBuilder.resultMaps(resultMaps);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
