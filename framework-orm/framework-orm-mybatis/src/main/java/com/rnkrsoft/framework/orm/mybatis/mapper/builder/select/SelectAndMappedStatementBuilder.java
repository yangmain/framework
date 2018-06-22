package com.rnkrsoft.framework.orm.mybatis.mapper.builder.select;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.jdbc.select.JdbcSelectMapper;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.WhereSqlNode;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
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

/**
 * Created by devops4j on 2016/12/18.
 * 按照AND非null条件的字段查询MS建造器
 */
public class SelectAndMappedStatementBuilder extends MappedStatementBuilder {

    public SelectAndMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig,  mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, JdbcSelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, JdbcSelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        EntityExtractorHelper helper = new EntityExtractorHelper();
        Map<String, ColumnMetadata> fields = getTableMetadata().getColumnMetadataSet();
        String select = convert("SELECT", getOrmConfig().getKeywordMode());
        String from = convert("FROM", getOrmConfig().getKeywordMode());
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.generateSqlHead(entityClass, getOrmConfig().getKeywordMode(), getOrmConfig().getSqlMode(), false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode())).append(" ");
        //创建结果映射
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        List<SqlNode> wheres = new ArrayList<SqlNode>();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            String whereSql = convert(" AND ", getOrmConfig().getKeywordMode()) + convert(columnMetadata.getJdbcName(), getOrmConfig().getSqlMode()) + " = #{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            SqlNode node = new IfSqlNode(new TextSqlNode(whereSql), MessageFormat.format("{0} != null", columnMetadata.getJavaName()));
            wheres.add(node);
            parameterMappings.add(new ParameterMapping.Builder(config, columnMetadata.getJavaName(), registry.getTypeHandler(columnMetadata.getJavaType())).javaType(columnMetadata.getJavaType()).jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType())).build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", entityClass, parameterMappings);
        SqlNode whereSqlNode = new WhereSqlNode(config, new MixedSqlNode(wheres), getOrmConfig().getKeywordMode());
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString()), whereSqlNode));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.SELECT_AND, sqlSource, SqlCommandType.SELECT);
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
