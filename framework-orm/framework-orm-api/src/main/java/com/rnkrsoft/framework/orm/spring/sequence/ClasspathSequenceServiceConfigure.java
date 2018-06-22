package com.rnkrsoft.framework.orm.spring.sequence;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;

/**
 * Created by rnkrsoft.com on 2017/1/8.
 * 基于classpath的序号服务配置
 */
public class ClasspathSequenceServiceConfigure extends AbstractSequenceServiceConfigure {
    @Override
    public SequenceService register(String tableName, String className) {
        SequenceService instance = SequenceServiceFactory.instance(className);
        init(instance);
        initDataSource(instance);
        return instance;
    }
}
