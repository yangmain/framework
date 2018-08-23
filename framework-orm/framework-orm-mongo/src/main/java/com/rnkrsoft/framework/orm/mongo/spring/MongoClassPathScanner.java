package com.rnkrsoft.framework.orm.mongo.spring;

import com.mongodb.*;
import com.rnkrsoft.framework.orm.extractor.GenericsExtractor;
import com.rnkrsoft.framework.orm.mongo.MongoMapper;
import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import com.rnkrsoft.framework.orm.mongo.count.MongoCountMapper;
import com.rnkrsoft.framework.orm.mongo.insert.MongoInsertMapper;
import com.rnkrsoft.framework.orm.mongo.select.MongoSelectMapper;
import com.rnkrsoft.framework.orm.mongo.update.MongoUpdateMapper;
import com.rnkrsoft.utils.StringUtils;
import lombok.Getter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/6/27.
 */
public class MongoClassPathScanner extends ClassPathBeanDefinitionScanner {
    MongoMapperFactoryBean mongoMapperFactoryBean;
    @Setter
    Class mongoInterface;
    @Setter
    String schema;
    @Setter
    String username;
    @Setter
    String password;
    @Setter
    String connectionUri;
    final List<String> addresses = new ArrayList();

    public List<String> getAddresses() {
        return addresses;
    }

    public void setMongoMapperFactoryBean(MongoMapperFactoryBean mongoMapperFactoryBean) {
        this.mongoMapperFactoryBean = mongoMapperFactoryBean == null ? new MongoMapperFactoryBean() : mongoMapperFactoryBean;
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
        MongoClient mongoClient = null;
        //如果填写了连接URI则使用URI初始化
        if (StringUtils.isNotBlank(connectionUri)) {
            mongoClient = new MongoClient(new MongoClientURI(connectionUri));
        } else {
            final List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
            MongoCredential credential = MongoCredential.createCredential(username, schema, password.toCharArray());
            mongoClient = new MongoClient(serverAddresses, credential, MongoClientOptions.builder().build());
        }
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating MongoMapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }
            String mapperClassName = definition.getBeanClassName();

            Class mongoMapper = null;
            try {
                mongoMapper = Class.forName(mapperClassName);
            } catch (ClassNotFoundException e) {

            }
            Class entityClass = GenericsExtractor.extractEntityClass(mongoMapper, MongoMapper.class, MongoInsertMapper.class, MongoUpdateMapper.class, MongoSelectMapper.class, MongoCountMapper.class);

            definition.setBeanClass(this.mongoMapperFactoryBean.getClass());
            definition.getPropertyValues().add("mongoMapper", mapperClassName);
            definition.getPropertyValues().add("entityClass", entityClass);
            definition.getPropertyValues().add("mongoDaoClient", new MongoDaoClient(mongoClient, schema, entityClass));
            if (logger.isDebugEnabled()) {
                logger.debug("Enabling autowire by type for MongoMapperFactoryBean with name '" + holder.getBeanName() + "'.");
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
            logger.warn("Skipping MongoMapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mongoInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }
}
