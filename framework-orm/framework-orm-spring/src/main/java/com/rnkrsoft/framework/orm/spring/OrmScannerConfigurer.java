package com.rnkrsoft.framework.orm.spring;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.devops4j.message.MessageFormatter;
import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.orm.MapperMakerInterface;
import com.rnkrsoft.framework.orm.config.ItemConfig;
import com.rnkrsoft.framework.orm.config.OrmConfig;
import com.rnkrsoft.framework.orm.mybatis.spring.mapper.OrmClassPathMapperScanner;
import com.rnkrsoft.framework.orm.mybatis.spring.mapper.OrmMapperFactoryBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

/**
 * Created by rnkrsoft.com on 2018/4/2.
 * ORM扫描配置对象
 */
@Slf4j
public class OrmScannerConfigurer  implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
    /**
     * ORM配置对象
     */
    @Setter
    @Getter
    OrmConfig ormConfig;
    /**
     * SqlSessionFactory工厂Bean名称
     */
    @Setter
    String ormSessionFactoryBeanName = StringUtils.firstCharToLower(OrmSessionFactoryBean.class.getSimpleName());

    @Setter
    OrmMapperFactoryBean ormMapperFactoryBean;
    /**
     * 标记接口
     */
    @Setter
    Class<?> markerInterface;


    /**
     * Spring 上下文
     */
    @Setter
    ApplicationContext applicationContext;
    /**
     * Bean名称
     */
    @Setter
    String beanName;

    /**
     * 配置Bean定义完成后，使用OrmClassPathMapperScanner进行Mapper扫描，扫描所有具有标记接口的Mapper
     *
     * @param registry Spring 注册对象
     * @throws BeansException 异常
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (this.ormMapperFactoryBean == null) {
            this.ormMapperFactoryBean = new OrmMapperFactoryBean();
        }
        OrmClassPathMapperScanner scanner = new OrmClassPathMapperScanner(registry);
        scanner.setOrmConfig(this.ormConfig);
        scanner.setOrmSessionFactoryBeanName(this.ormSessionFactoryBeanName);
        scanner.setOrmMapperFactoryBean(this.ormMapperFactoryBean);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setMarkerInterface(this.markerInterface);
        scanner.registerFilters();
        String[] daoPackages = ormConfig.getDaoPackages();
        scanner.doScan(daoPackages);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * 根据获取的配置路径
     * 如果为类全限定名时，进行加载类测试；
     * 如果为包路径时，使用classpath机制进行扫描其下的所有*.class。
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.ormConfig, "Property 'ormConfig' is required");
        notNull(this.ormConfig.getDaoPackages(), "Property 'ormConfig.daoPackages' is required");
        notEmpty(this.ormConfig.getDaoPackages(), "Property 'ormConfig.daoPackages' is empty");
        Map<String, ItemConfig> configMap = new HashMap();
        if (ormConfig.getConfigs() != null) {
            for (String basePackage : ormConfig.getConfigs().keySet()) {
                if(log.isDebugEnabled()){
                    log.info("scanning daoPackage '{}'", basePackage);
                }
                ItemConfig itemConfig = ormConfig.getConfigs().get(basePackage);
                if(log.isDebugEnabled()){
                    log.info("scanning daoPackage '{}' '{}'", basePackage, itemConfig);
                }
                //检测key是否为接口
                try {
                    Class.forName(basePackage);
                    configMap.put(basePackage, itemConfig);
                    continue;
                } catch (ClassNotFoundException e) {
                    //包名的情况
                    String basePackage_path = basePackage.replaceAll("\\.", "/");
                    //进行数组拆分
                    String[] basePackages = org.springframework.util.StringUtils.tokenizeToStringArray(basePackage_path, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
                    //检测key是否为通配符
                    for (String basePackage0 : basePackages) {
                        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
                        Resource[] resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage0 + "/" + DEFAULT_RESOURCE_PATTERN);
                        for (Resource resource : resources) {
                            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            ClassMetadata classMetadata = metadataReader.getClassMetadata();
                            if (classMetadata.isInterface()) {
                                Class clazz = Class.forName(classMetadata.getClassName());
                                if (MapperMakerInterface.class.isAssignableFrom(clazz)) {
                                    if (!configMap.containsKey(classMetadata.getClassName())) {
                                        configMap.put(classMetadata.getClassName(), itemConfig);
                                    } else {
                                        throw new Error(MessageFormatter.format("存在冲突的配置[{}]", basePackage));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        ormConfig.setConfigs(configMap);
        //通过Spring容器获取
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean(this.ormSessionFactoryBeanName);
        Configuration configuration = sqlSessionFactory.getConfiguration();
        for (Resource mapperLocation : ormConfig.getMapperLocations()){
            if (mapperLocation == null) {
                continue;
            }
            if (!mapperLocation.exists()) {
                log.warn("mapper file [{}] is not exists!", mapperLocation.getFile().getName());
                continue;
            }
            try {
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(), configuration, mapperLocation.toString(), configuration.getSqlFragments());
                xmlMapperBuilder.parse();
            } catch (Exception e) {
                throw ErrorContextFactory.instance()
                        .message("Failed to parse mapping resource: '{}'", mapperLocation)
                        .cause(e)
                        .runtimeException();
            } finally {
                ErrorContextFactory.instance().reset();
            }

            if (log.isDebugEnabled()) {
                log.debug("Parsed mapper file: '" + mapperLocation + "'");
            }
        }
        if(ormConfig.getGlobal() == null){
            throw ErrorContextFactory.instance()
                    .activity("OrmScannerConfigurer init")
                    .message("ormConfig.global is null")
                    .solution("the property  which name is 'global' can not be null in the 'OrmScannerConfigurer'")
                    .runtimeException();
        }
    }
}
