package com.rnkrsoft.framework.orm.mybatis.sequence;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.sequence.DataSourceAware;
import com.rnkrsoft.framework.sequence.SequenceService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rnkrsoft.com on 2017/1/8.
 * 抽象的序号服务配置对象
 */
public abstract class AbstractSequenceServiceConfigure implements SequenceServiceConfigure {
    @Setter
    protected Properties mappings;

    @Setter
    protected Properties parameters;

    @Autowired(required = false)
    @Qualifier("defaultDataSource")
    DataSource dataSource;

    protected static Map<String, SequenceService> TABLE_INSTANCES = new ConcurrentHashMap();

    @Override
    public SequenceService getSequenceService(String tableName) {
        SequenceService sequenceService = null;
        if ((sequenceService = TABLE_INSTANCES.get(tableName.toUpperCase())) != null) {
            return sequenceService;
        }
        synchronized (this) {
            if ((sequenceService = TABLE_INSTANCES.get(tableName.toUpperCase())) != null) {
                return sequenceService;
            }
            String sequenceServiceClassName = mappings.getProperty(tableName);
            if (sequenceServiceClassName == null || sequenceServiceClassName.isEmpty()) {
                throw ErrorContextFactory.instance()
                        .activity("初始化表{}序号服务", tableName.toUpperCase())
                        .message("获取序号服务发生错误，因为属性'mappings'中没有配置序号服务映射")
                        .solution("在序号服务'mappings'中配置表名->序号服务类的映射").runtimeException();
            }
            sequenceService = register(tableName.toUpperCase(), sequenceServiceClassName);
            return sequenceService;
        }
    }

    protected void init(SequenceService sequenceService){
        for (String tableName : mappings.stringPropertyNames()) {
            String sequenceServiceClassName = mappings.getProperty(tableName);
            if (sequenceServiceClassName == null || sequenceServiceClassName.isEmpty()) {
                throw ErrorContextFactory.instance()
                        .activity("初始化表{}序号服务", tableName.toUpperCase())
                        .message("获取序号服务发生错误，因为属性'mappings'中没有配置序号服务映射")
                        .solution("在序号服务'mappings'中配置表名->序号服务类的映射").runtimeException();
            }

            TABLE_INSTANCES.put(tableName.toUpperCase(), sequenceService);
        }
    }
    /**
     * 对序号服务设置数据源
     *
     * @param sequenceService 序号服务
     * @return 序号服务
     */
    protected SequenceService initDataSource(SequenceService sequenceService) {
        if (sequenceService instanceof DataSourceAware) {
            DataSourceAware dataSourceAware = (DataSourceAware) sequenceService;
            dataSourceAware.setDataSource(dataSource);
        }
        return sequenceService;
    }
}
