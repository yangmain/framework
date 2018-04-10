package com.rnkrsoft.framework.orm.mybatis.sequence;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * Created by devops4j on 2017/1/8.
 * 基于Spring的序号服务配置
 */
public class SpringSequenceServiceConfigure extends AbstractSequenceServiceConfigure implements InitializingBean {
    @Autowired(required = false)
    @Qualifier("defaultDataSource")
    @Setter
    DataSource dataSource;

    @Override
    public SequenceService register(String tableName, String className) {
        SequenceService instance = SequenceServiceFactory.instance(className);
        return initDataSource(instance);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(this.mappings == null){
            Assert.notNull(this.mappings, "Property 'mappings' is required");
        }else{
            for (String key : mappings.stringPropertyNames()){
                String key0 = key.toUpperCase();
                String val = mappings.getProperty(key);
                mappings.remove(key);
                mappings.setProperty(key0, val);
            }
        }
        if (this.parameters == null) {
            Assert.notNull(this.parameters, "Property 'parameters' is required");
        }
    }
}
