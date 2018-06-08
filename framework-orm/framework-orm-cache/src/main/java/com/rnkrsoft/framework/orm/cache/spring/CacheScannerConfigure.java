package com.rnkrsoft.framework.orm.cache.spring;

import com.devops4j.reflection4j.resource.ClassScanner;
import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.orm.cache.Cache;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by rnkrsoft.com on 2018/6/6.
 */
public class CacheScannerConfigure implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
    ApplicationContext applicationContext;
    String beanName;
    @Setter
    String[] basePackages;
    CacheClient cacheClient;
    @Setter
    String host = "127.0.0.1";
    @Setter
    int port = 4796;
    @Setter
    int index = 0;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassScanner classScanner = new ClassScanner();
        for (String basePackage: basePackages){
            classScanner.scan(basePackage, new ClassScanner.AnnotatedWithFilter(Cache.class));
        }
        for (Class clazz :classScanner.getClasses()){
            CacheMapperFactoryBean cacheMapperFactoryBean = new CacheMapperFactoryBean(clazz, cacheClient);
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(cacheMapperFactoryBean.getClass());    //设置类
            definition.setScope("singleton");       //设置scope
            definition.setLazyInit(false);          //设置是否懒加载
            definition.setAutowireCandidate(true);  //设置是否可以被其他对象自动注入
            registry.registerBeanDefinition(clazz.getName(), definition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
