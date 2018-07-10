package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import lombok.Getter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/6/27.
 */
public class MongoClassPathScanner extends ClassPathBeanDefinitionScanner {
    @Setter
    MongoMapperFactoryBean mongoMapperFactoryBean;
    @Setter
    Class mongoMarkInterface;
    @Getter
    final List<String> hosts = new ArrayList();

    @Setter
    String schema;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;


    public void setMongoMapperFactoryBean(MongoMapperFactoryBean mongoMapperFactoryBean) {
        this.mongoMapperFactoryBean = mongoMapperFactoryBean;
    }

    public MongoClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        if (hosts.size() == 1) {
            String host = hosts.get(0);
            String[] strs = host.split(":");
            this.mongoClient = new MongoClient(new ServerAddress(strs[0], Integer.valueOf(strs[1])), MongoClientOptions.builder().build());
            if (this.schema != null){
                this.mongoDatabase = this.mongoClient.getDatabase(this.schema);
            }
        }
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;
        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.mongoMarkInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.mongoMarkInterface) {
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

    void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions){
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating MongoMapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }
            String mapperClassName = definition.getBeanClassName();
            Class mapperClass = null;
            try {
                mapperClass = Class.forName(mapperClassName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            definition.getPropertyValues().add("mongoDaoClient", new MongoDaoClient(this.mongoClient, this.mongoDatabase, mapperClass));
            definition.getPropertyValues().add("mongoInterface", mapperClassName);
            definition.setBeanClass(this.mongoMapperFactoryBean.getClass());
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            registerBeanDefinition(holder, getRegistry());
        }
    }
}
