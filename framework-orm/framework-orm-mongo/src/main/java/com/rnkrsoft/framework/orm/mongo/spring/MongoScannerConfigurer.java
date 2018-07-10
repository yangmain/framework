package com.rnkrsoft.framework.orm.mongo.spring;

import com.rnkrsoft.framework.orm.mongo.MongoInterface;
import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;

/**
 * Created by rnkrsoft.com on 2018/6/27.
 */
public class MongoScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
    @Setter
    ApplicationContext applicationContext;
    @Setter
    String beanName;
    @Setter
    String[] basePackages;
    @Setter
    Class<?> mongoMarkInterface;
    @Setter
    String[] hosts;
    @Setter
    String schema;
    @Setter
    MongoMapperFactoryBean mongoMapperFactoryBean;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        MongoClassPathScanner scanner = new MongoClassPathScanner(registry);
        scanner.setMongoMarkInterface(this.mongoMarkInterface == null ? MongoInterface.class : this.mongoMarkInterface);
        scanner.hosts.addAll(Arrays.asList(hosts));
        scanner.setSchema(schema);
        scanner.setMongoMapperFactoryBean(this.mongoMapperFactoryBean);
        scanner.registerFilters();
        scanner.doScan(this.basePackages);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (hosts == null || hosts.length == 0){
            throw ErrorContextFactory.instance().message("MongoDB 数据库地址未配置").runtimeException();
        }
        if (basePackages == null || basePackages.length == 0){
            throw ErrorContextFactory.instance().message("MongoDB DAO所在包未配置").runtimeException();
        }
    }
}
