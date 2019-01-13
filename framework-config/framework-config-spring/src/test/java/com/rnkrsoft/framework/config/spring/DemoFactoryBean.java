package com.rnkrsoft.framework.config.spring;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * Created by rnkrsoft.com on 2018/3/1.
 * 9:05
 */
@Data
public class DemoFactoryBean implements FactoryBean<Demo>, BeanDefinitionRegistryPostProcessor {
    String name;
    Integer age;

    @Override
    public Demo getObject() throws Exception {
        Demo demo = new Demo();
        demo.setName(name);
        demo.setAge(age);
        return demo;
    }

    @Override
    public Class<?> getObjectType() {
        return Demo.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println(name);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(name);
    }
}
