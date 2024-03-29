package com.rnkrsoft.framework.orm.mybatis.mapper.builder.select;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.jdbc.select.JdbcSelectMapper;
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
public class SelectPageOrMappedStatementBuilder extends MappedStatementBuilder {

    public SelectPageOrMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig, mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, JdbcSelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, JdbcSelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        String select = convert("SELECT", getOrmConfig().getKeywordMode());
        String from = convert("FROM", getOrmConfig().getKeywordMode());
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.generateSqlHead(entityClass, getOrmConfig().getKeywordMode(), getOrmConfig().getSqlMode(), false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode())).append(" ");
        List<ParameterMapping> parameterMappings = new ArrayList();
        List<SqlNode> wheres = new ArrayList();
        Map<String, ColumnMetadata> fields = getTableMetadata().getColumnMetadataSet();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            String whereSql = convert(" OR ", getOrmConfig().getKeywordMode()) + convert(columnMetadata.getJdbcName(), getOrmConfig().getSqlMode()) + " = #{entity." + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType().getCode() + " }";
            SqlNode node = new IfSqlNode(new TextSqlNode(whereSql), MessageFormat.format("entity.{0} != null", columnMetadata.getJavaName()));
            wheres.add(node);
            parameterMappings.add(new ParameterMapping.Builder(config, columnMetadata.getJavaName(), registry.getTypeHandler(columnMetadata.getJavaType())).javaType(columnMetadata.getJavaType()).jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType().getCode())).build());
        }
        SqlNode whereSqlNode = new WhereSqlNode(config, new MixedSqlNode(wheres), getOrmConfig().getKeywordMode());
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString()), whereSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.SELECT_PAGE_OR, sqlSource, SqlCommandType.SELECT);
        msBuilder = msBuilder.flushCacheRequired(true).useCache(false);
        //创建参数映射
        for (String column: fields.keySet()){
            ColumnMetadata columnMetadata = fields.get(column);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, "entity."+ columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType().getCode()));
            parameterMappings.add(builder.build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "PageParameterMap", entityClass, parameterMappings);
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        List<ResultMapping> resultMappings = new ArrayList();
        for (String column: fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ResultMapping.Builder builder = new ResultMapping.Builder(config, columnMetadata.getJavaName(), convert(columnMetadata.getJdbcName(), getOrmConfig().getSqlMode()), columnMetadata.getJavaType());
            resultMappings.add(builder.build());
        }
        final ResultMap resultMap = new ResultMap.Builder(config, "PageBaseResultMap", entityClass, resultMappings).build();
        List<ResultMap> resultMaps = new ArrayList();
        resultMaps.add(resultMap);
        msBuilder.resultMaps(resultMaps);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
