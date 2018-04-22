package com.rnkrsoft.framework.orm.mybatis.mapper.builder.update;
import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rnkrsoft.framework.orm.untils.KeywordsUtils.convert;

/**
 * Created by devops4j on 2016/12/18.
 * 按照主键更新MS建造器
 */
public class UpdateByPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {

    public UpdateByPrimaryKeyMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig, mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        String primaryKeyName = getTableMetadata().getPrimaryKeys().get(0);
        Map<String, ColumnMetadata> fields = getTableMetadata().getColumnMetadataSet();
        ColumnMetadata primaryKeyColumn = fields.get(primaryKeyName);
        String update = convert("UPDATE", getOrmConfig().getKeywordMode());
        String where = convert("WHERE", getOrmConfig().getKeywordMode());
        //headBuilder是前半段
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(update).append(" ");
        headBuilder.append(convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode())).append(" ");

        //footBuilder是后半段
        String primaryKeySql = "#{" + primaryKeyColumn.getJavaName() + ":" + primaryKeyColumn.getJdbcType() + " }";
        StringBuilder footBuilder = new StringBuilder();
        footBuilder.append(where).append(" ");
        footBuilder.append(convert(primaryKeyName, getOrmConfig().getSqlMode())).append(" = ").append(primaryKeySql);
        //生成Set部分
        List<SqlNode> sets = new ArrayList();
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList();
        for (String column : getTableMetadata().getOrderColumns()) {
            //如果是主键，没必要更新
            if (column.equals(primaryKeyName)) {
                continue;
            }
            ColumnMetadata columnMetadata = fields.get(column);
            //生成Set
            String valueSql = "#{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " }";
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(convert(column, getOrmConfig().getSqlMode()))
                    .append(" = ")
                    .append(valueSql)
                    .append(" , ");
            sets.add(new TextSqlNode(sqlBuilder.toString()));
            //创建参数映射
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType()));
            parameterMappings.add(builder.build());
        }
        DynamicSqlSource sqlSource = new DynamicSqlSource(config
                , mixedContents(new TextSqlNode(headBuilder.toString())
                , new TrimSqlNode(config, new MixedSqlNode(sets), convert("SET", getOrmConfig().getKeywordMode()), "", "", ",")
                , new TextSqlNode(footBuilder.toString())));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.UPDATE_BY_PRIMARY_KEY, sqlSource, SqlCommandType.UPDATE);
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "BaseParameterMap", entityClass, parameterMappings);
        msBuilder.parameterMap(paramBuilder.build());
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
