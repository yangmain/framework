package com.rnkrsoft.framework.orm.mybatis.mapper.builder.select;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rnkrsoft.framework.orm.untils.KeywordsUtils.convert;

/**
 * Created by devops4j on 2016/12/18.
 * 按照主键查询MS建造器
 */
public class SelectByPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {

    public SelectByPrimaryKeyMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig, mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        String primaryKeyName = getTableMetadata().getPrimaryKeys().get(0);
        Map<String, ColumnMetadata> fields = getTableMetadata().getColumnMetadataSet();
        ColumnMetadata primaryKeyColumn = fields.get(primaryKeyName);
        String select = convert("SELECT", getOrmConfig().getKeywordMode());
        String from = convert("FROM", getOrmConfig().getKeywordMode());
        String where = convert("WHERE", getOrmConfig().getKeywordMode());
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ");
        sqlBuilder.append(SqlScriptUtils.generateSqlHead(entityClass, getOrmConfig().getKeywordMode(), getOrmConfig().getSqlMode(), false)).append(" ");
        sqlBuilder.append(from).append(" ");
        sqlBuilder.append(convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode())).append(" ");
        sqlBuilder.append(where).append(" ");
        sqlBuilder.append(convert(primaryKeyName, getOrmConfig().getSqlMode())).append(" = ? ");
        StaticSqlSource sqlSource = new StaticSqlSource(config, sqlBuilder.toString());
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.SELECT_BY_PRIMARY_KEY, sqlSource, SqlCommandType.SELECT);
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
