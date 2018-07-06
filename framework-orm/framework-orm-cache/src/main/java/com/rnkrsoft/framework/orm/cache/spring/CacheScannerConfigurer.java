package com.rnkrsoft.framework.orm.cache.spring;

import com.rnkrsoft.framework.cache.client.CacheClient;
import com.rnkrsoft.framework.cache.client.CacheClientSetting;
import com.rnkrsoft.framework.cache.client.RedisType;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by rnkrsoft.com on 2018/6/6.
 */
public class CacheScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
    @Setter
    ApplicationContext applicationContext;
    @Setter
    String beanName;
    @Setter
    CacheMapperFactoryBean cacheMapperFactoryBean;
    @Setter
    String[] basePackages;
    @Setter
    Class<?> cacheInterface;
    CacheClient cacheClient;
    @Setter
    String host = "127.0.0.1:6479";
    @Setter
    String password;
    @Setter
    int index = 0;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        CacheClassPathScanner scanner = new CacheClassPathScanner(registry);
        scanner.setCacheInterface(this.cacheInterface);
        this.cacheClient = new CacheClient();
        this.cacheClient.init(CacheClientSetting.builder().host(host).password(password).databaseIndex(index).redisType(RedisType.AUTO).build());
        scanner.setCacheClient(this.cacheClient);
        scanner.setCacheMapperFactoryBean(this.cacheMapperFactoryBean);
        scanner.registerFilters();
        scanner.doScan(basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
