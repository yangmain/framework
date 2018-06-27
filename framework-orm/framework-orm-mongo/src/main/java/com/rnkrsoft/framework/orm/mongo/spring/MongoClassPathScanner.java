package com.rnkrsoft.framework.orm.mongo.spring;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * Created by woate on 2018/6/27.
 */
public class MongoClassPathScanner extends ClassPathBeanDefinitionScanner {
    public MongoClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {

    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return null;
    }
}
