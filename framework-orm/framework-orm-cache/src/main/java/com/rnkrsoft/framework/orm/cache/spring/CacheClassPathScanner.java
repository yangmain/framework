package com.rnkrsoft.framework.orm.cache.spring;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/6/7.
 */
public class CacheClassPathScanner extends ClassPathBeanDefinitionScanner{

    CacheMapperFactoryBean cacheMapperFactoryBean;

    public CacheClassPathScanner setCacheMapperFactoryBean(CacheMapperFactoryBean cacheMapperFactoryBean) {
        this.cacheMapperFactoryBean = cacheMapperFactoryBean;
        return this;
    }

    public CacheClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No Cache mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating CacheMapperFactoryBean with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is OrmMapperFactoryBean
            definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
            definition.setBeanClass(this.cacheMapperFactoryBean.getClass());

            boolean explicitFactoryUsed = false;

            if (!explicitFactoryUsed) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Enabling autowire by type for CacheMapperFactoryBean with name '" + holder.getBeanName() + "'.");
                }
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            }
        }
    }

}
