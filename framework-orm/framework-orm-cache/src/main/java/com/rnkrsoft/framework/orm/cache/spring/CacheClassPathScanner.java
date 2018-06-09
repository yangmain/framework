package com.rnkrsoft.framework.orm.cache.spring;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.orm.cache.CacheMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/6/7.
 */
public class CacheClassPathScanner extends ClassPathBeanDefinitionScanner {
    CacheMapperFactoryBean cacheMapperFactoryBean;
    @Setter
    Class cacheInterface = CacheMapper.class;
    @Setter
    CacheClient cacheClient;

    public CacheClassPathScanner setCacheMapperFactoryBean(CacheMapperFactoryBean cacheMapperFactoryBean) {
        this.cacheMapperFactoryBean = (cacheMapperFactoryBean != null ? cacheMapperFactoryBean : new CacheMapperFactoryBean());
        return this;
    }

    public CacheClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;
        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.cacheInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.cacheInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
            addIncludeFilter(new TypeFilter() {
                @Override
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    return true;
                }
            });
        }

        // exclude package-info.java
        addExcludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                String className = metadataReader.getClassMetadata().getClassName();
                return className.endsWith("package-info");
            }
        });
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
                logger.debug("Creating CacheMapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is OrmMapperFactoryBean
            definition.getPropertyValues().add("cacheInterface", definition.getBeanClassName());
            definition.setBeanClass(this.cacheMapperFactoryBean.getClass());
            definition.getPropertyValues().add("cacheClient", this.cacheClient);
            if (logger.isDebugEnabled()) {
                logger.debug("Enabling autowire by type for CacheMapperFactoryBean with name '" + holder.getBeanName() + "'.");
            }
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registerBeanDefinition(holder, getRegistry());
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.warn("Skipping OrmMapperFactoryBean with name '" + beanName
                    + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }

}
