package com.rnkrsoft.framework.orm.mybatis.mapper.builder.insert;

import com.devops4j.utils.DateStyle;
import com.devops4j.utils.DateUtils;
import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.PrimaryKeyFeatureConstant;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.extractor.GenericityExtractor;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.orm.mybatis.mapper.builder.MappedStatementBuilder;
import com.rnkrsoft.framework.orm.select.SelectMapper;
import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.orm.untils.EntityExtractorUtils;
import com.rnkrsoft.framework.orm.untils.KeywordsUtils;
import com.devops4j.logtrace4j.ErrorContextFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import com.rnkrsoft.framework.orm.mybatis.sequence.SequenceServiceConfigure;


import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.*;

/**
 * Created by devops4j on 2016/12/18.
 * 全字段插入MS建造器
 */
@Slf4j
public class InsertMappedStatementBuilder extends MappedStatementBuilder {
    /**
     * 序号服务配置对象
     */
    protected SequenceServiceConfigure sequenceConfigure;

    public InsertMappedStatementBuilder(Configuration config, Class mapperClass, SequenceServiceConfigure sequenceConfigure) {
        super(config, mapperClass.getName(), mapperClass, GenericityExtractor.extractEntityClass(mapperClass, SelectMapper.class), GenericityExtractor.extractKeyClass(mapperClass, SelectMapper.class));
        this.sequenceConfigure = sequenceConfigure;
    }

    @Override
    public MappedStatement build() {
        TableMetadata tableMetadata = EntityExtractorUtils.extractTable(entityClass, strict);
        TextSqlNode insertIntoSqlNode = new TextSqlNode(KeywordsUtils.convert("INSERT INTO ", keywordMode) + KeywordsUtils.convert(tableMetadata.getTableName(), keywordMode) + "(");
        List<SqlNode> heads = new ArrayList();
        List<SqlNode> values = new ArrayList();
        Map<String, ColumnMetadata> fields = tableMetadata.getColumnMetadatas();
        for (String column : tableMetadata.getOrderColumns()) {
            ColumnMetadata columnMetadata = fields.get(column);
            heads.add(new TextSqlNode(KeywordsUtils.convert(columnMetadata.getJdbcName(), sqlMode) + ","));
            String valueExp = "#{" + columnMetadata.getJavaName() + ":" + columnMetadata.getJdbcType() + " },";
            values.add(new TextSqlNode(valueExp));
        }
        MixedSqlNode headsSqlNode = new MixedSqlNode(heads);
        MixedSqlNode valuesSqlNode = new MixedSqlNode(values);
        DynamicSqlSource sqlSource = new DynamicSqlSource(config, mixedContents(insertIntoSqlNode
                , new TrimSqlNode(config, headsSqlNode, "", "", "", ",")//用于去除多余的逗号
                , new TextSqlNode(") ")
                , new TextSqlNode(KeywordsUtils.convert("VALUES", keywordMode))
                , new TextSqlNode(" (")
                , new TrimSqlNode(config, valuesSqlNode, "", "", "", ",")//用于去除多余的逗号
                , new TextSqlNode(")")));

        //创建一个MappedStatement建造器
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(config, namespace + "." + Constants.INSERT, sqlSource, SqlCommandType.INSERT);
        msBuilder = msBuilder.flushCacheRequired(true).useCache(false);
        //创建参数映射
        List<ParameterMapping> parameterMappings = new ArrayList();
        for (String column : fields.keySet()) {
            ColumnMetadata columnMetadata = fields.get(column);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(config, columnMetadata.getJavaName(), columnMetadata.getJavaType());
            builder.jdbcType(JdbcType.valueOf(columnMetadata.getJdbcType()));
            parameterMappings.add(builder.build());
        }
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(config, "BaseParameterMap", entityClass, parameterMappings);
        List<String> primaryKeys = tableMetadata.getPrimaryKeys();
        //存在主键，肯定的
        if (!primaryKeys.isEmpty()) {
            final String pkColumonName = primaryKeys.get(0);
            final ColumnMetadata primaryKeyMetadata = fields.get(pkColumonName);
            //如果主键是自增整数主键，则使用主键自动生成
            if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.IDENTITY) {
                log.debug("use int increment");
                msBuilder.keyColumn(primaryKeyMetadata.getJdbcName());
                msBuilder.keyProperty(primaryKeyMetadata.getJavaName());
                msBuilder.keyGenerator(new Jdbc3KeyGenerator());
            } else if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.UUID) {
                log.debug("use uuid");
                final Class entityClass1 = this.entityClass;
                final String getterMethodName = "get" + StringUtils.firstCharToUpper(primaryKeyMetadata.getJavaName());
                final String setterMethodName = "set" + StringUtils.firstCharToUpper(primaryKeyMetadata.getJavaName());
                //将主键值设置到实体
                Method getMethod0 = null;
                Method setMethod0 = null;
                try {
                    getMethod0 = entityClass1.getMethod(getterMethodName, new Class[0]);
                    setMethod0 = entityClass1.getMethod(setterMethodName, new Class[]{primaryKeyMetadata.getJavaType()});
                } catch (NoSuchMethodException e) {
                    ErrorContextFactory.instance()
                            .activity("正在使用devops4j orm 的自动生成主键")
                            .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                            .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName())
                            .cause(e).throwError();
                }
                final Method setMethod = setMethod0;
                final Method getMethod = getMethod0;
                msBuilder.keyGenerator(new KeyGenerator() {
                    @Override
                    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
                        try {
                            if (getMethod.invoke(parameter) != null) {
                                return;
                            }
                            //将主键值设置到实体
                            setMethod.invoke(parameter, UUID.randomUUID().toString());
                        } catch (Exception e) {
                            ErrorContextFactory.instance()
                                    .activity("正在使用devops4j orm 的自动生成主键")
                                    .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                                    .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName()).throwError();
                            //这个异常一般不会发生
                        }
                    }

                    @Override
                    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

                    }
                });
            }else if(primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.SEQUENCE_SERVICE){
                log.debug("use sequence");
                final String schema = primaryKeyMetadata.getTableMetadata().getSchema();
                final String tableName = primaryKeyMetadata.getTableMetadata().getTableName();
                final String tablePrefix = KeywordsUtils.convert(tableName.substring(0, tableName.indexOf("_")), sqlMode);
                final String feature = primaryKeyMetadata.getPrimaryKeyFeature();
                final String getterMethodName = "get" + StringUtils.firstCharToUpper(primaryKeyMetadata.getJavaName());
                final String setterMethodName = "set" + StringUtils.firstCharToUpper(primaryKeyMetadata.getJavaName());
                //将主键值设置到实体
                Method setMethod0 = null;
                try {
                    setMethod0 = tableMetadata.getEntityClass().getMethod(setterMethodName, new Class[]{primaryKeyMetadata.getJavaType()});
                } catch (NoSuchMethodException e) {
                    ErrorContextFactory.instance()
                            .activity("正在使用devops4j orm 的自动生成主键")
                            .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                            .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName())
                            .cause(e).throwError();
                }
                final Method setMethod = setMethod0;
                String seqFeature0 = null;
                if (feature.equals(PrimaryKeyFeatureConstant.CURRENT_DATE)) {
                    seqFeature0 = DateUtils.toFullString(new Date());
                } else if (feature.equals(PrimaryKeyFeatureConstant.yyyy_MM_dd_HH_mm_ss_SSS)) {
                    seqFeature0 = DateUtils.toString(new Date(), DateStyle.FILE_FORMAT2);
                } else if (feature.equals(PrimaryKeyFeatureConstant.yyyy_MM_dd_HH_mm_ss)) {
                    seqFeature0 = DateUtils.toString(new Date(), DateStyle.FILE_FORMAT3);
                } else if (feature.equals(PrimaryKeyFeatureConstant.yyyy_MM_dd_HH_mm)) {
                    seqFeature0 = DateUtils.toString(new Date(), DateStyle.FILE_FORMAT4);
                } else if (feature.equals(PrimaryKeyFeatureConstant.yyyy_MM_dd_HH)) {
                    seqFeature0 = DateUtils.toString(new Date(), DateStyle.FILE_FORMAT5);
                } else if (feature.equals(PrimaryKeyFeatureConstant.yyyy_MM_dd)) {
                    seqFeature0 = DateUtils.toString(new Date(), DateStyle.FILE_FORMAT6);
                } else {
                    seqFeature0 = feature;
                }
                final String seqFeature = seqFeature0;
                final SequenceService sequenceService = sequenceConfigure.getSequenceService(tableMetadata.getTableName());
                msBuilder.keyGenerator(new KeyGenerator() {
                    @Override
                    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

                        int pk = 0;
                        try {
                            pk = sequenceService.nextval(schema, tablePrefix, tableName, seqFeature);
                        } catch (Exception e) {
                            ErrorContextFactory.instance()
                                    .activity("正在使用devops4j orm 的自动生成主键")
                                    .message("生成序列号发生异常")
                                    .solution("检查序列号介质是否存在，在'autoCreate'属性设置为true").throwError();
                            //这个异常一般不会发生
                        }
                        try {
                            setMethod.invoke(parameter, pk);
                        } catch (Exception e) {
                            ErrorContextFactory.instance()
                                    .activity("正在使用devops4j orm 的自动生成主键")
                                    .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                                    .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName()).throwError();
                            //这个异常一般不会发生
                        }

                    }

                    @Override
                    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

                    }
                });
            }
        }
        msBuilder.parameterMap(paramBuilder.build());
        //创建结果映射
        //建造出MappedStatement
        MappedStatement ms = msBuilder.build();
        return ms;
    }
}
