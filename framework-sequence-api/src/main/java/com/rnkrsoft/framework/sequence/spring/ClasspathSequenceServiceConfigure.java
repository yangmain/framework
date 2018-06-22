package com.rnkrsoft.framework.sequence.spring;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;

import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2017/1/8.
 * 基于classpath的序号服务配置
 */
public class ClasspathSequenceServiceConfigure extends AbstractSequenceServiceConfigure {
    public ClasspathSequenceServiceConfigure() {
    }

    public ClasspathSequenceServiceConfigure(Properties mappings) {
        this.mappings = mappings;

    }

    @Override
    public SequenceService register(String tableName, String className) {
        SequenceService instance = SequenceServiceFactory.instance(className);
        init(instance);
        initDataSource(instance);
        return instance;
    }
}
