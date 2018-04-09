package com.rnkrsoft.framework.orm.mybatis.sequence;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;
import lombok.Setter;

import javax.activation.DataSource;
import java.lang.reflect.Method;

/**
 * Created by rnkrsoft.com on 2017/1/8.
 * 基于classpath的序号服务配置
 */
public class ClasspathSequenceServiceConfigure extends AbstractSequenceServiceConfigure {
    @Setter
    DataSource dataSource;
    @Override
    public SequenceService register(String tableName, String className) {
        SequenceService instance = SequenceServiceFactory.instance(className);
        return initDataSource(instance);
    }
}
