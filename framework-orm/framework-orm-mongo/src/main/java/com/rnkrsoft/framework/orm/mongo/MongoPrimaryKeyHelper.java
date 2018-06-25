package com.rnkrsoft.framework.orm.mongo;

import com.rnkrsoft.framework.orm.PrimaryKeyFeatureConstant;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.expression.ExpressionContext;
import com.rnkrsoft.framework.orm.expression.ExpressionHelper;
import com.rnkrsoft.framework.orm.metadata.ColumnMetadata;
import com.rnkrsoft.framework.orm.metadata.TableMetadata;
import com.rnkrsoft.framework.sequence.spring.SequenceServiceConfigure;
import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.reflection4j.GlobalSystemMetadata;
import com.rnkrsoft.reflection4j.Invoker;
import com.rnkrsoft.reflection4j.MetaClass;
import com.rnkrsoft.time.DateStyle;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/6/21.
 */
@Slf4j
public class MongoPrimaryKeyHelper {
    /**
     * 生成物理主键
     *
     * @param ssc                序号服务配置对象
     * @param tableMetadata      表元信息
     * @param primaryKeyMetadata 物理主键元信息
     */
    public static <T> T generate(final SequenceServiceConfigure ssc, final TableMetadata tableMetadata, final ColumnMetadata primaryKeyMetadata) {
        if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.IDENTITY) {
            return null;
        }else  if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.UUID) {
            return (T) UUID.randomUUID().toString();
        }else if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.SEQUENCE_SERVICE){
            log.debug("{} use sequence", tableMetadata.getFullTableName());
            final String schema = primaryKeyMetadata.getTableMetadata().getSchema();
            final String tableName = primaryKeyMetadata.getTableMetadata().getTableName();
            final String feature = primaryKeyMetadata.getPrimaryKeyFeature();
            String seqFeature0 = null;
            if (feature == null) {
            } else if (feature.equals(PrimaryKeyFeatureConstant.YYYY_MM_DD_HH_MM_SS_SSS_SEQUEUE5)) {
                seqFeature0 = DateUtils.toString(new Date(), DateStyle.FILE_FORMAT2);
            } else {
                seqFeature0 = feature;
            }
            final String seqFeature = seqFeature0;
            final SequenceService sequenceService = ssc.getSequenceService(tableMetadata.getTableName());
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
            return (T) primaryKey;
        }else if (primaryKeyMetadata.getPrimaryKeyStrategy() == PrimaryKeyStrategy.EXPRESSION){
            log.debug("{} use expression", tableMetadata.getFullTableName());
            final String schema = primaryKeyMetadata.getTableMetadata().getSchema();
            final String tableName = primaryKeyMetadata.getTableMetadata().getTableName();
            final String feature = primaryKeyMetadata.getPrimaryKeyFeature();
            Object primaryKey = ExpressionHelper.eval(ExpressionContext.builder().useCache(true).expression(feature).schema(schema).sequenceName(tableName).build());
            return (T) primaryKey;
        }else{
            return null;
        }
    }
}
