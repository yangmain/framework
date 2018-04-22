package com.rnkrsoft.framework.orm.mybatis.mapper.builder.delete;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.orm.extractor.EntityExtractorHelper;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
@Slf4j
public class TruncateMappedStatementBuilder  extends MappedStatementBuilder {

    public TruncateMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig, mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, SelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        TypeHandlerRegistry registry = config.getTypeHandlerRegistry();
        String truncate = KeywordsUtils.convert("TRUNCATE TABLE ", getOrmConfig().getKeywordMode());
        String tableName = KeywordsUtils.convert(getTableMetadata().getFullTableName(), getOrmConfig().getSqlMode());
        StaticSqlSource sqlSource = new StaticSqlSource(config, truncate + tableName);
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.TRUNCATE, sqlSource, SqlCommandType.DELETE);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
