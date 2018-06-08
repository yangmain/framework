package com.rnkrsoft.framework.orm.mybatis.mapper.builder.count;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.jdbc.select.SelectMapper;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
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
 * 按照AND非null条件的字段统计MS建造器
 */
public class CountAndMappedStatementBuilder extends MappedStatementBuilder {

    public CountAndMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig,  mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        Map<String, ColumnMetadata> fields = getTableMetadata().getColumnMetadataSet();
        String select = KeywordsUtils.convert("SELECT", getOrmConfig().getKeywordMode());
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(KeywordsUtils.convert("COUNT(1) AS", getOrmConfig().getKeywordMode())).append(" ");
        sqlBuilder.append(KeywordsUtils.convert("CNT", getOrmConfig().getSqlMode())).append(" ");
        sqlBuilder.append(KeywordsUtils.convert("FROM", getOrmConfig().getKeywordMode())).append(" ");
        sqlBuilder.append(KeywordsUtils.convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode())).append(" ");
        //创建结果映射
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        List<SqlNode> wheres = new ArrayList<SqlNode>();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            String whereSql = KeywordsUtils.convert(" AND ", getOrmConfig().getKeywordMode()) + KeywordsUtils.convert(columnMetadata.getJdbcName(), getOrmConfig().getSqlMode()) + " = #{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            SqlNode node = new IfSqlNode(new TextSqlNode(whereSql), MessageFormat.format("{0} != null", columnMetadata.getJavaName()));
            wheres.add(node);
            parameterMappings.add(new ParameterMapping.Builder(config, columnMetadata.getJavaName(), registry.getTypeHandler(keyClass)).build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", entityClass, parameterMappings);
        SqlNode whereSqlNode = new WhereSqlNode(config, new MixedSqlNode(wheres), getOrmConfig().getKeywordMode());
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString()), whereSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.COUNT_AND, sqlSource, SqlCommandType.SELECT);
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
