package com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.extractor.GenericityExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.untils.EntityExtractorUtils;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class LockByForUpdateByPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {
    public LockByForUpdateByPrimaryKeyMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, GenericityExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericityExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        TableMetadata tableMetadata = EntityExtractorUtils.extractTable(entityClass, strict);
        String primaryKeyName = tableMetadata.getPrimaryKeys().get(0);
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        String select = KeywordsUtils.convert("SELECT", keywordMode);
        String from = KeywordsUtils.convert("FROM", keywordMode);
        String where = KeywordsUtils.convert("WHERE", keywordMode);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.genreateSqlHead(entityClass, keywordMode, sqlMode, false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(KeywordsUtils.convert(tableMetadata.getTableName(), sqlMode)).append(" ");
        sqlBuilder.append(where).append(" ");
        sqlBuilder.append(KeywordsUtils.convert(primaryKeyName, sqlMode)).append(" = ? ");
        sqlBuilder.append(" ").append(KeywordsUtils.convert("FOR UPDATE", keywordMode));
        StaticSqlSource sqlSource = new StaticSqlSource(config, sqlBuilder.toString());
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.LOCK_BY_FOR_UPDATE_BY_PRIMARY_KEY, sqlSource, SqlCommandType.SELECT);
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        parameterMappings.add(new ParameterMapping.Builder(config, primaryKeyName, registry.getTypeHandler(keyClass)).build());
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", entityClass, parameterMappings);
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
