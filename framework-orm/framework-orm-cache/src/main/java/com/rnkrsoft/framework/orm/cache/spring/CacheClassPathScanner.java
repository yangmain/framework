package com.rnkrsoft.framework.orm.cache.spring;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.orm.cache.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by rnkrsoft.com on 2018/6/7.
 */
@Slf4j
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
            String mapperClassName = definition.getBeanClassName();
            try {
                Class mapperClass = Class.forName(mapperClassName);
                Method[] methods = mapperClass.getMethods();
                for (Method method : methods) {
                    Get getAnnotation = method.getAnnotation(Get.class);
                    GetSet getSetAnnotation = method.getAnnotation(GetSet.class);
                    Incr incrAnnotation = method.getAnnotation(Incr.class);
                    Decr decrAnnotation = method.getAnnotation(Decr.class);
                    Expire expireAnnotation = method.getAnnotation(Expire.class);
                    Ttl ttlAnnotation = method.getAnnotation(Ttl.class);
                    Presist presistAnnotation = method.getAnnotation(Presist.class);
                    Keys keysAnnotation = method.getAnnotation(Keys.class);
                    Type typeAnnotation = method.getAnnotation(Type.class);
                    boolean annotation = false;
                    boolean match = false;
                    if (getAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() == Void.TYPE) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型为void", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", "XxxBean", method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length == 0) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 1) {
                            match = true;
                        } else if (method.getParameterTypes().length > 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (getSetAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() == Void.TYPE) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型为void", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key, {} val)", "XxxBean", method.getName(), "XxxBean")
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length < 2) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入不匹配", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key, {} val)", method.getReturnType().getSimpleName(), method.getName(), method.getReturnType().getSimpleName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 2) {
                            match = true;
                        } else if (method.getParameterTypes().length > 2) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key, {} val)", method.getReturnType().getSimpleName(), method.getName(), method.getReturnType().getSimpleName())
                                    .runtimeException();
                        }
                    }
                    if (incrAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() != Long.class) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型为void", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", Long.class.getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length == 0) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 1) {
                            match = true;
                        } else if (method.getParameterTypes().length > 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (decrAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() != Long.class) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型为void", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", Long.class.getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length == 0) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 1) {
                            match = true;
                        } else if (method.getParameterTypes().length > 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (expireAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() != Void.TYPE) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型为void", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key, int second)", Void.class.getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length < 2) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key, int second)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 2) {
                            match = true;
                        } else if (method.getParameterTypes().length > 2) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key, int second)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (ttlAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() != Long.class) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型为void", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", Long.class.getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length == 0) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 1) {
                            match = true;
                        } else if (method.getParameterTypes().length > 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (presistAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() != Void.TYPE) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型为void", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", Void.class.getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length < 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 1) {
                            match = true;
                        } else if (method.getParameterTypes().length > 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (keysAnnotation != null) {
                        annotation = true;
                        if (!Arrays.asList(List.class, java.util.Set.class).contains(method.getReturnType())) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型不为List或Set", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String pattern)", List.class.getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length < 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String pattern)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 1) {
                            match = true;
                        } else if (method.getParameterTypes().length > 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String pattern)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (typeAnnotation != null) {
                        annotation = true;
                        if (method.getReturnType() != Class.class) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数返回类型不为Class", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", Class.class.getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                        if (method.getParameterTypes().length < 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}无参数输入", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        } else if (method.getParameterTypes().length == 1) {
                            match = true;
                        } else if (method.getParameterTypes().length > 1) {
                            throw ErrorContextFactory.instance()
                                    .message("{}.{}参数输入过多", mapperClassName, method.getName())
                                    .solution("修改为public {} {}(String key)", method.getReturnType().getSimpleName(), method.getName())
                                    .runtimeException();
                        }
                    }
                    if (annotation) {
                        if (match) {
                            log.info("{}.{} match CacheMapper", mapperClassName, method.getName());
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is OrmMapperFactoryBean
            definition.getPropertyValues().add("cacheInterface", mapperClassName);
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
