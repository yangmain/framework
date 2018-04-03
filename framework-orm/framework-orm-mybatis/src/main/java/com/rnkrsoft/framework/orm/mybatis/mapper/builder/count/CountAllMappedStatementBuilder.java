package com.rnkrsoft.framework.orm.mybatis.mapper.builder.count;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.extractor.GenericityExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.untils.EntityExtractorUtils;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by devops4j on 2016/12/18.
 * 统计全部条数MS建造器
 */
public class CountAllMappedStatementBuilder extends MappedStatementBuilder {

    public CountAllMappedStatementBuilder(Configuration config, Class mapperClass) {
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
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(new StaticTextSqlNode(sqlBuilder.toString())));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.COUNT_ALL, sqlSource, SqlCommandType.SELECT);
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
