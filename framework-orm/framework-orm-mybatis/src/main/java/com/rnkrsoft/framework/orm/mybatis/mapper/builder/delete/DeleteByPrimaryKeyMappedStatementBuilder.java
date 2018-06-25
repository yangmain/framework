package com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.jdbc.select.JdbcSelectMapper;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devops4j on 2016/12/18.
 * 按照主键删除MS建造器
 */
public class DeleteByPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {

    public DeleteByPrimaryKeyMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig, mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, JdbcSelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, JdbcSelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        String primaryKeyName = getTableMetadata().getPrimaryKeys().get(0);
        String delete = KeywordsUtils.convert("DELETE FROM", getOrmConfig().getKeywordMode());
        String where = KeywordsUtils.convert("WHERE", getOrmConfig().getKeywordMode());
        //headBuilder是前半段
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(delete).append(" ");
        headBuilder.append(KeywordsUtils.convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode())).append(" ");

        //footBuilder是后半段
        StringBuilder footBuilder = new StringBuilder();
        footBuilder.append(where).append(" ");
        footBuilder.append(KeywordsUtils.convert(primaryKeyName, getOrmConfig().getSqlMode())).append(" = ?");
        DynamicSqlSource sqlSource = new DynamicSqlSource(config
                , mixedContents(new TextSqlNode(headBuilder.toString())
                , new TextSqlNode(footBuilder.toString())));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.DELETE_BY_PRIMARY_KEY, sqlSource, SqlCommandType.DELETE);
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList();
        parameterMappings.add(new ParameterMapping.Builder(config, primaryKeyName, registry.getTypeHandler(keyClass)).build());
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "defaultParameterMap", entityClass, parameterMappings);
        msBuilder.parameterMap(paramBuilder.build());
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
