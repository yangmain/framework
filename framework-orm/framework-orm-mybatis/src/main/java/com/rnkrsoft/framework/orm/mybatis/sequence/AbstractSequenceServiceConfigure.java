package com.rnkrsoft.framework.orm.mybatis.sequence;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.sequence.SequenceService;
import lombok.Setter;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by devops4j on 2017/1/8.
 * 抽象的序号服务配置对象
 */
public abstract class AbstractSequenceServiceConfigure implements SequenceServiceConfigure {
    @Setter
    protected Properties mappings;
    @Setter
    protected Properties parameters;
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
                ErrorContextFactory.instance()
                        .activity("初始化表{}序号服务", tableName.toUpperCase())
                        .message("获取序号服务发生错误，因为属性'mappings'中没有配置序号服务映射")
                        .solution("在序号服务'mappings'中配置表名->序号服务类的映射").throwError();
                return null;
            }
            sequenceService = register(tableName.toUpperCase(), sequenceServiceClassName);
            TABLE_INSTANCES.put(tableName.toUpperCase(), sequenceService);
            return sequenceService;
        }
    }

    /**
     * 对序号服务设置数据源
     * @param sequenceService 序号服务
     * @return 序号服务
     */
    protected SequenceService initDataSource(SequenceService sequenceService){
        for (String tableName : mappings.stringPropertyNames()){
            String sequenceServiceClassName = mappings.getProperty(tableName);
            if (sequenceServiceClassName == null || sequenceServiceClassName.isEmpty()) {
                ErrorContextFactory.instance()
                        .activity("初始化表{}序号服务", tableName.toUpperCase())
                        .message("获取序号服务发生错误，因为属性'mappings'中没有配置序号服务映射")
                        .solution("在序号服务'mappings'中配置表名->序号服务类的映射").throwError();
                return null;
            }
            sequenceService = register(tableName.toUpperCase(), sequenceServiceClassName);
            TABLE_INSTANCES.put(tableName.toUpperCase(), sequenceService);
        }
        return sequenceService;
    }
}
