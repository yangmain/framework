package com.rnkrsoft.framework.orm.mybatis.sequence;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Invoker;
import com.rnkrsoft.reflection4j.MetaClass;
import com.rnkrsoft.time.DateStyle;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.utils.StringUtils;
import com.rnkrsoft.framework.orm.PrimaryKeyFeatureConstant;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.expression.ExpressionContext;
import com.rnkrsoft.framework.orm.expression.ExpressionHelper;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.sequence.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/4/9.
 */
@Slf4j
public class PrimaryKeyHelper {
    /**
     * 生成物理主键
     *
     * @param ssc                序号服务配置对象
     * @param tableMetadata      表元信息
     * @param primaryKeyMetadata 物理主键元信息
     * @param msBuilder          已映射语句块
     */
    public static void generate(final SequenceServiceConfigure ssc, final TableMetadata tableMetadata, final ColumnMetadata primaryKeyMetadata, MappedStatement.Builder msBuilder) {
        //如果主键是自增整数主键，则使用主键自动生成
        if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.IDENTITY) {
            log.debug("{} use int increment", tableMetadata.getFullTableName());
            msBuilder.keyColumn(primaryKeyMetadata.getJdbcName());
            msBuilder.keyProperty(primaryKeyMetadata.getJavaName());
            msBuilder.keyGenerator(new Jdbc3KeyGenerator());
        } else if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.UUID) {
            log.debug("{} use uuid", tableMetadata.getFullTableName());
            final String getterMethodName = "get" + StringUtils.firstCharToUpper(primaryKeyMetadata.getJavaName());
            final String setterMethodName = "set" + StringUtils.firstCharToUpper(primaryKeyMetadata.getJavaName());
            //将主键值设置到实体
            Method getMethod0 = null;
            Method setMethod0 = null;
            try {
                getMethod0 = tableMetadata.getEntityClass().getMethod(getterMethodName, new Class[0]);
                setMethod0 = tableMetadata.getEntityClass().getMethod(setterMethodName, new Class[]{primaryKeyMetadata.getJavaType()});
            } catch (NoSuchMethodException e) {
                throw ErrorContextFactory.instance()
                        .activity("正在使用devops4j orm 的自动生成主键")
                        .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                        .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName())
                        .cause(e)
                        .runtimeException();
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
                        throw ErrorContextFactory.instance()
                                .activity("正在使用devops4j orm 的自动生成主键")
                                .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                                .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName())
                                .cause(e)
                                .runtimeException();
                        //这个异常一般不会发生
                    }
                }

                @Override
                public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

                }
            });
        } else if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.SEQUENCE_SERVICE) {
            log.debug("{} use sequence", tableMetadata.getFullTableName());
            final String schema = primaryKeyMetadata.getTableMetadata().getSchema();
            final String tableName = primaryKeyMetadata.getTableMetadata().getTableName();
            final String feature = primaryKeyMetadata.getPrimaryKeyFeature();
            MetaClass metaClass = GlobalSystemMetadata.forClass(tableMetadata.getEntityClass());
            //将主键值设置到实体
            final Invoker invoker = metaClass.getSetter(primaryKeyMetadata.getJavaName());
            String seqFeature0 = null;
            if (feature == null) {
            } else if (feature.equals(PrimaryKeyFeatureConstant.YYYY_MM_DD_HH_MM_SS_SSS_SEQUEUE5)) {
                seqFeature0 = DateUtils.toString(new Date(), DateStyle.FILE_FORMAT2);
            } else {
                seqFeature0 = feature;
            }
            final String seqFeature = seqFeature0;
            final SequenceService sequenceService = ssc.getSequenceService(tableMetadata.getTableName());
            msBuilder.keyGenerator(new KeyGenerator() {
                @Override
                public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
                    Object primaryKey = null;
                    if (primaryKeyMetadata.getJavaType() == String.class) {
                        Integer seqNo = null;
                        try {
                            seqNo = sequenceService.nextval(schema, tableMetadata.getPrefix(), tableName, seqFeature);
                        } catch (Exception e) {
                            throw ErrorContextFactory.instance()
                                    .activity("正在使用 orm 的自动生成主键")
                                    .message("生成序列号发生异常")
                                    .solution("检查序列号介质是否存在，在'autoCreate'属性设置为true")
                                    .cause(e)
                                    .runtimeException();
                            //这个异常一般不会发生
                        }
                        if (feature == null) {
                            if (primaryKeyMetadata.getJavaType() == String.class) {
                                primaryKey = StringUtils.fill(Integer.toString(seqNo), true, '0', 5);
                            } else {
                                primaryKey = seqNo;
                            }

                        } else if (feature.equals(PrimaryKeyFeatureConstant.YYYY_MM_DD_HH_MM_SS_SSS_SEQUEUE5)) {
                            primaryKey = seqFeature + StringUtils.fill(Integer.toString(seqNo), true, '0', 5);
                        }
                    } else if (primaryKeyMetadata.getJavaType() == Integer.class || primaryKeyMetadata.getJavaType() == Integer.TYPE) {
                        try {
                            primaryKey = sequenceService.nextval(schema, tableMetadata.getPrefix(), tableName, seqFeature);
                        } catch (Exception e) {
                            throw ErrorContextFactory.instance()
                                    .activity("正在使用 orm 的自动生成主键")
                                    .message("生成序列号发生异常")
                                    .solution("检查序列号介质是否存在，在'autoCreate'属性设置为true")
                                    .cause(e)
                                    .runtimeException();
                            //这个异常一般不会发生
                        }

                    }
                    try {
                        invoker.invoke(parameter, primaryKey);
                    } catch (Exception e) {
                        throw ErrorContextFactory.instance()
                                .activity("正在使用 orm 的自动生成主键")
                                .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                                .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName())
                                .cause(e)
                                .runtimeException();
                        //这个异常一般不会发生
                    }
                }

                @Override
                public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

                }
            });
        } else if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.EXPRESSION) {
            log.debug("{} use expression", tableMetadata.getFullTableName());
            final String schema = primaryKeyMetadata.getTableMetadata().getSchema();
            final String tableName = primaryKeyMetadata.getTableMetadata().getTableName();
            final String feature = primaryKeyMetadata.getPrimaryKeyFeature();
            MetaClass metaClass = GlobalSystemMetadata.forClass(tableMetadata.getEntityClass());
            //将主键值设置到实体
            final Invoker invoker = metaClass.getSetter(primaryKeyMetadata.getJavaName());
            msBuilder.keyGenerator(new KeyGenerator() {
                @Override
                public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
                    Object primaryKey = ExpressionHelper.eval(ExpressionContext.builder().useCache(true).expression(feature).schema(schema).sequenceName(tableName).build());
                    try {
                        invoker.invoke(parameter, primaryKey);
                    } catch (Exception e) {
                        throw ErrorContextFactory.instance()
                                .activity("正在使用 orm 的自动生成主键")
                                .message("获取设置字段{}主键值发生错误", primaryKeyMetadata.getJavaName())
                                .solution("检查实体{}字段{}是否为public", primaryKeyMetadata.getEntityClass(), primaryKeyMetadata.getJavaName())
                                .cause(e)
                                .runtimeException();
                        //这个异常一般不会发生
                    }
                }

                @Override
                public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

                }
            });
        }
    }
}
