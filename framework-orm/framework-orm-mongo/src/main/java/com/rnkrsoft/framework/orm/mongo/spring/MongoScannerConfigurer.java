package com.rnkrsoft.framework.orm.mongo.spring;

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
    Class<?> mongoInterface;
    @Setter
    String schema;
    @Setter
    String[] addresses;
    @Setter
    String username;
    @Setter
    String password;
    @Setter
    String connectionUri;
    @Setter
    MongoMapperFactoryBean mongoMapperFactoryBean;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        MongoClassPathScanner scanner = new MongoClassPathScanner(registry);
        scanner.setMongoInterface(this.mongoInterface);
        scanner.setSchema(schema);
        scanner.setUsername(username);
        scanner.setPassword(password);
        if (addresses != null && addresses.length > 0) {
            scanner.getAddresses().addAll(Arrays.asList(addresses));
        }
        scanner.setConnectionUri(connectionUri);
        scanner.setMongoMapperFactoryBean(this.mongoMapperFactoryBean);
        scanner.registerFilters();
        scanner.doScan(this.basePackages);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
