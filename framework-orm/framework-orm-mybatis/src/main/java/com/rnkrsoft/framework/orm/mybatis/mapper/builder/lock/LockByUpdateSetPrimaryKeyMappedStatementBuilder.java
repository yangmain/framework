package com.rnkrsoft.framework.orm.mybatis.mapper.builder.lock;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.jdbc.select.JdbcSelectMapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.Map;

import static com.rnkrsoft.framework.orm.untils.KeywordsUtils.convert;

/**
 * Created by devops4j on 2016/12/18.
 */
@Slf4j
public class LockByUpdateSetPrimaryKeyMappedStatementBuilder extends MappedStatementBuilder {

    public LockByUpdateSetPrimaryKeyMappedStatementBuilder(Configuration config, OrmConfig ormConfig, Class mapperClass) {
        super(config, ormConfig, mapperClass.getName(), mapperClass, GenericsExtractor.extractEntityClass(mapperClass, JdbcSelectMapper.class), GenericsExtractor.extractKeyClass(mapperClass, JdbcSelectMapper.class));
    }

    @Override
    public MappedStatement build() {
        String primaryKeyName = getTableMetadata().getPrimaryKeys().get(0);
        Map<String, ColumnMetadata> fields = getTableMetadata().getColumnMetadataSet();
        ColumnMetadata primaryKeyColumn = fields.get(primaryKeyName);
        String update = convert("UPDATE", getOrmConfig().getKeywordMode());
        //headBuilder是前半段
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append(update).append(" ");
        headBuilder.append(convert(getTableMetadata().getFullTableName(), getOrmConfig().getKeywordMode())).append(" ");
        headBuilder.append(convert("SET", getOrmConfig().getKeywordMode())).append(" ");
        headBuilder.append(convert(primaryKeyColumn.getJdbcName(), getOrmConfig().getSqlMode())).append(" ");
        headBuilder.append("=");
        headBuilder.append(convert(primaryKeyColumn.getJdbcName(), getOrmConfig().getSqlMode())).append(" ");

        //footBuilder是后半段
        String where = convert("WHERE", getOrmConfig().getKeywordMode());
        String primaryKeySql = "#{" + primaryKeyColumn.getJavaName() + ":" + primaryKeyColumn.getJdbcType().getCode() + " }";
        StringBuilder footBuilder = new StringBuilder();
        footBuilder.append(where).append(" ");
        footBuilder.append(convert(primaryKeyName, getOrmConfig().getSqlMode())).append(" = ").append(primaryKeySql);
        DynamicSqlSource sqlSource = new DynamicSqlSource(config
                , mixedContents(new TextSqlNode(headBuilder.toString())
                , new TextSqlNode(footBuilder.toString())));
        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.LOCK_BY_UPDATE_SET_PRIMARY_KEY, sqlSource, SqlCommandType.UPDATE);
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
