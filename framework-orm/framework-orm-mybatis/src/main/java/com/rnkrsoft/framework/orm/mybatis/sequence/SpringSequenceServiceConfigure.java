package com.rnkrsoft.framework.orm.mybatis.sequence;

import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2017/1/8.
 * 基于Spring的序号服务配置
 */
public class SpringSequenceServiceConfigure extends AbstractSequenceServiceConfigure implements InitializingBean {

    @Override
    public SequenceService register(String tableName, String className) {
        SequenceService instance = SequenceServiceFactory.instance(className);
        init(instance);
        initDataSource(instance);
        return instance;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.mappings == null) {
            this.mappings = new Properties();
        } else {
            for (String key : this.mappings.stringPropertyNames()) {
                String key0 = key.toUpperCase();
                String val = this.mappings.getProperty(key);
                this.mappings.remove(key);
                this.mappings.setProperty(key0, val);
            }
        }
    }
}
