package com.rnkrsoft.framework.orm.mybatis.spring.mapper;

import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.mybatis.spring.OrmSessionTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 * classpath Mapper扫描器
 */
@Slf4j
@Setter
public class OrmClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    /**
     * 配置对象
     */
    @Getter
    OrmConfig ormConfig;


    private SqlSessionFactory ormSessionFactory;

    private OrmSessionTemplate ormSessionTemplate;

    private String ormSessionTemplateBeanName;

    private String ormSessionFactoryBeanName;

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    private OrmMapperFactoryBean ormMapperFactoryBean = new OrmMapperFactoryBean();

    public OrmClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setOrmMapperFactoryBean(OrmMapperFactoryBean ormMapperFactoryBean) {
        this.ormMapperFactoryBean = (ormMapperFactoryBean != null ? ormMapperFactoryBean : new OrmMapperFactoryBean());
    }


    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
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

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as
     * MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            log.warn("No ORM mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (log.isDebugEnabled()) {
                log.debug("Creating OrmMapperFactoryBean with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is OrmMapperFactoryBean
            definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
            definition.setBeanClass(this.ormMapperFactoryBean.getClass());

            definition.getPropertyValues().add("ormConfig", this.ormConfig);

            boolean explicitFactoryUsed = false;
            if (StringUtils.hasText(this.ormSessionFactoryBeanName)) {
                definition.getPropertyValues().add("ormSessionFactory", new RuntimeBeanReference(this.ormSessionFactoryBeanName));
                explicitFactoryUsed = true;
            } else if (this.ormSessionFactory != null) {
                definition.getPropertyValues().add("ormSessionFactory", this.ormSessionFactory);
                explicitFactoryUsed = true;
            }

            if (StringUtils.hasText(this.ormSessionTemplateBeanName)) {
                if (explicitFactoryUsed) {
                    log.warn("Cannot use both: ormSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
                }
                definition.getPropertyValues().add("ormSessionTemplate", new RuntimeBeanReference(this.ormSessionTemplateBeanName));
                explicitFactoryUsed = true;
            } else if (this.ormSessionTemplate != null) {
                if (explicitFactoryUsed) {
                    log.warn("Cannot use both: ormSessionTemplate and sqlSessionFactory together. sqlSessionFactory is ignored.");
                }
                definition.getPropertyValues().add("ormSessionTemplate", this.ormSessionTemplate);
                explicitFactoryUsed = true;
            }

            if (!explicitFactoryUsed) {
                if (log.isDebugEnabled()) {
                    log.debug("Enabling autowire by type for OrmMapperFactoryBean with name '" + holder.getBeanName() + "'.");
                }
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            }
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
            log.warn("Skipping OrmMapperFactoryBean with name '" + beanName
                    + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }
}
