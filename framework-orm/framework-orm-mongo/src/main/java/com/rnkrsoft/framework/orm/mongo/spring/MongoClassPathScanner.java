package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.mongo.MongoMapper;
import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import com.rnkrsoft.framework.orm.mongo.count.MongoCountMapper;
import com.rnkrsoft.framework.orm.mongo.insert.MongoInsertMapper;
import com.rnkrsoft.framework.orm.mongo.select.MongoSelectMapper;
import com.rnkrsoft.framework.orm.mongo.update.MongoUpdateMapper;
import lombok.Setter;
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
 * Created by woate on 2018/6/27.
 */
public class MongoClassPathScanner extends ClassPathBeanDefinitionScanner {
    @Setter
    MongoMapperFactoryBean mongoMapperFactoryBean;
    @Setter
    Class mongoInterface;
    String schema;
    @Setter
    String host;


    public void setMongoMapperFactoryBean(MongoMapperFactoryBean mongoMapperFactoryBean) {
        this.mongoMapperFactoryBean = mongoMapperFactoryBean;
    }

    public MongoClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;
        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.mongoInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.mongoInterface) {
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
            logger.warn("No Mongo mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, 3017), MongoClientOptions.builder().build());
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating MongoMapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }
            String mapperClassName = definition.getBeanClassName();
           Class mongoMapper =  definition.getBeanClass();
            Class entityClass = GenericsExtractor.extractEntityClass(mongoMapper, MongoMapper.class, MongoInsertMapper.class, MongoUpdateMapper.class, MongoSelectMapper.class, MongoCountMapper.class);
            definition.setBeanClass(this.mongoMapperFactoryBean.getClass());
            definition.getPropertyValues().add("mongoInterface", mapperClassName);
            MongoDaoClient mongoDaoClient = new MongoDaoClient(mongoClient, schema, entityClass);
            definition.getPropertyValues().add("mongoDaoClient", mongoDaoClient);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registerBeanDefinition(holder, getRegistry());
        }
    }
}
