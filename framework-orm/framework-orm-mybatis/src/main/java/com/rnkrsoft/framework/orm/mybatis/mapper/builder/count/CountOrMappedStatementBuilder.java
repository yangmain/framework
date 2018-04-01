package com.rnkrsoft.framework.orm.mybatis.mapper.builder.count;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.extractor.GenericityExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.untils.EntityExtractorUtils;
import com.rnkrsoft.framework.untils.KeywordsUtils;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.WhereSqlNode;
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
 * 按照OR非null条件的字段查询MS建造器
 */
public class CountOrMappedStatementBuilder extends MappedStatementBuilder {
    public CountOrMappedStatementBuilder(Configuration config, Class mapperClass) {
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
        sqlBuilder.append(KeywordsUtils.convert("COUNT(1) AS", keywordMode)).append(" ");
        sqlBuilder.append(KeywordsUtils.convert("CNT", sqlMode)).append(" ");
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
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString()), whereSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.COUNT_OR, sqlSource, SqlCommandType.SELECT);
        //创建参数映射
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
        ResultMapping.Builder builder = new ResultMapping.Builder(config, "count", "CNT", int.class);
        resultMappings.add(builder.build());
        final ResultMap resultMap = new ResultMap.Builder(config, "BaseResultMap", int.class, resultMappings).build();
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(resultMap);
        msBuilder.resultMaps(resultMaps);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
