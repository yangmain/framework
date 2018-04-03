package com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.extractor.GenericityExtractor;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.untils.EntityExtractorUtils;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
@Slf4j
public class TruncateMappedStatementBuilder  extends MappedStatementBuilder {

    public TruncateMappedStatementBuilder(Configuration config, Class mapperClass) {
        super(config, mapperClass.getName(), mapperClass, GenericityExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericityExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        TableMetadata tableMetadata = EntityExtractorUtils.extractTable(entityClass, strictWing4j);
        String truncate = KeywordsUtils.convert("TRUNCATE TABLE ", keywordMode);
        String tableName = KeywordsUtils.convert(tableMetadata.getTableName(), keywordMode);
        StaticSqlSource sqlSource = new StaticSqlSource(config, truncate + tableName);
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.TRUNCATE, sqlSource, SqlCommandType.DELETE);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
