package com.rnkrsoft.framework.config.spring;

import com.rnkrsoft.framework.config.utils.ValueUtils;
import com.rnkrsoft.io.buffer.ByteBuf;
import com.rnkrsoft.message.MessageFormatter;
import com.rnkrsoft.framework.config.annotation.DynamicFile;
import com.rnkrsoft.framework.config.annotation.DynamicParam;
import com.rnkrsoft.framework.config.client.ConfigClient;
import com.rnkrsoft.framework.config.client.ConfigClientSetting;
import com.rnkrsoft.framework.config.spring.aspect.DynamicConfigAspectJ;
import com.rnkrsoft.framework.config.utils.FileSystemUtils;
import com.rnkrsoft.framework.config.utils.MachineUtils;
import com.rnkrsoft.framework.config.utils.PropertiesUtils;
import com.rnkrsoft.framework.config.v1.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by rnkrsoft.com on 2017/2/12.
 * 动态方式的属性占位符
 */
@Slf4j
public class DynamicConfigSourceConfigurer extends PropertySourcesPlaceholderConfigurer implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware, EnvironmentAware, BeanPostProcessor {

    @Setter
    MutablePropertySources propertySources;

    PropertySources appliedPropertySources;

    @Setter
    Environment environment;

    @Setter
    ConnectorType connectorType;
    /**
     * 配置模式
     */
    @Setter
    RuntimeMode runtimeMode;
    /**
     * 配置中心主机地址
     */
    @Setter
    String host = "localhost";
    /**
     * 配置中心主机端口号
     */
    @Setter
    int port = 80;
    /**
     * 组织编号
     */
    @Setter
    String groupId;
    /**
     * 组件编号
     */
    @Setter
    String artifactId;
    /**
     * 版本
     */
    @Setter
    String version;
    /**
     * 环境
     */
    @Setter
    String env;

    @Setter
    String securityKey;
    /**
     * 本地配置文件根目录
     */
    @Setter
    String localFileHome;
    /**
     * 工作目录
     */
    @Setter
    String workHome;
    /**
     * 文件编码
     */
    @Setter
    String fileEncoding = "UTF-8";
    /**
     * 拉取参数间隔秒数
     */
    @Setter
    long fetchIntervalSeconds = 60;
    /**
     * 项目描述信息
     */
    @Setter
    String description;
    /**
     * 最大的重试时间间隔
     */
    @Setter
    int maxRetryIntervalSeconds = 0;
    /**
     * 是否罗嗦模式
     */
    @Setter
    boolean verbose = false;
    /**
     * 配置中心
     */
    ConfigClient configClient;

    BeanFactory beanFactory;
    /**
     * 引导属性配置
     */
    @Setter
    Properties bootProperties = new Properties();
    /**
     * 运行时属性配置
     */
    final Properties runtimeProperties = new Properties();

    PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);

    public DynamicConfigSourceConfigurer() {
        super.setFileEncoding(this.fileEncoding);
    }

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        synchronized (DynamicConfigSourceConfigurer.class) {
            Map<String, String> envs = System.getenv();
            List<String> keys = new ArrayList<String>(envs.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                log.info("ENV VAR: {}({})", key, envs.get(key));
            }
            if (envs.containsKey("CONFIG_HOST")) {
                this.host = envs.get("CONFIG_HOST");
                log.info("[host] uses os env var config '{}'!", host);
            } else {
                log.info("[host] uses applicationContext-config.xml config '{}'!", host);
            }
            if (envs.containsKey("CONFIG_PORT")) {
                this.port = Integer.valueOf(envs.get("CONFIG_PORT"));
                log.info("[port] uses os env var config '{}'!", port);
            } else {
                log.info("[port] uses applicationContext-config.xml config '{}'!", port);
            }
            if (envs.containsKey("CONFIG_ENV")) {
                this.env = envs.get("CONFIG_ENV");
                log.info("[env] uses os env var config '{}'!", env);
            } else {
                log.info("[env] uses applicationContext-config.xml config '{}'!", env);
            }
            if (envs.containsKey("CONFIG_RUNTIME_MODE")) {
                this.runtimeMode = RuntimeMode.valueOf(envs.get("CONFIG_RUNTIME_MODE"));
                log.info("[runtimeMode] uses os env var config '{}'!", runtimeMode);
            } else {
                log.info("[runtimeMode] uses applicationContext-config.xml config '{}'!", runtimeMode);
            }
            if (envs.containsKey("CONFIG_WORK_HOME")) {
                this.workHome = envs.get("CONFIG_WORK_HOME");
                log.info("[workHome] uses os env var config '{}'!", workHome);
            } else {
                log.info("[workHome] uses applicationContext-config.xml config '{}'!", workHome);
            }
            if (envs.containsKey("CONFIG_LOCAL_FILE_HOME")) {
                this.localFileHome = envs.get("CONFIG_LOCAL_FILE_HOME");
                log.info("[localFileHome] uses os env var config '{}'!", localFileHome);
            } else {
                log.info("[localFileHome] uses applicationContext-config.xml config '{}'!", localFileHome);
            }

            if (envs.containsKey("CONFIG_SECURITY_KEY")) {
                this.securityKey = envs.get("CONFIG_SECURITY_KEY");
                log.info("[securityKey] uses os env var config '{}'!", securityKey);
            } else {
                log.info("[securityKey] uses applicationContext-config.xml config '{}'!", localFileHome);
            }
        }
        //检验参数
        verification();
        if (RuntimeMode.LOCAL == runtimeMode) {//如果是本地模式，则按父类方式处理即可
            try {
                super.postProcessBeanFactory(beanFactory);
                //记载引导参数
                //加载location指定的Properties文件
                bootProperties = PropertiesUtils.clone(mergeProperties());
                //将null转换成空串
                convertProperties(bootProperties);
                for (Object key : bootProperties.keySet()) {
                    String propKey = (String) key;
                    this.runtimeProperties.setProperty(propKey, bootProperties.getProperty(propKey));
                }
                return;
            } catch (Exception e) {
                log.error("init happens error!", e);
                throw new RuntimeException(e);
            }
        } else {
            try {
                if (this.propertySources == null) {
                    this.propertySources = new MutablePropertySources();
                    if (this.environment != null) {
                        this.propertySources.addLast(
                                new PropertySource<Environment>(ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME, this.environment) {
                                    @Override
                                    public String getProperty(String key) {
                                        return this.source.getProperty(key);
                                    }
                                }
                        );
                    }
                    //自动模式是将本地参数进行初始化，再调用远程进行初始化
                    if (RuntimeMode.AUTO == runtimeMode) {
                        //记载引导参数
                        //加载location指定的Properties文件
                        bootProperties = PropertiesUtils.clone(mergeProperties());
                        //将null转换成空串
                        convertProperties(bootProperties);
                        for (Object key : bootProperties.keySet()) {
                            String propKey = (String) key;
                            this.runtimeProperties.setProperty(propKey, bootProperties.getProperty(propKey));
                        }
                    }
                    //从远程拉取配置
                    if (RuntimeMode.AUTO == runtimeMode || RuntimeMode.REMOTE == runtimeMode) {
                        //最终解析出的坐标
                        log.info("load remote's config [groupId, artifactId , version, env] -> [{}, {}, {}, {}]", groupId, artifactId, version, env);
                        //初始化配置中心客户端
                        initConfigCenter();
                        //转换属性
                        convertProperties(this.configClient.getProperties());
                    }
                    PropertySource<?> localPropertySource = new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, this.configClient.getProperties());
                    if (this.localOverride) {
                        this.propertySources.addFirst(localPropertySource);
                    } else {
                        this.propertySources.addLast(localPropertySource);
                    }
                }
                //处理参数
                processProperties(beanFactory, new PropertySourcesPropertyResolver(this.propertySources));
                this.appliedPropertySources = this.propertySources;
            } catch (Exception e) {
                log.error("init happens error!", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 初始化配置中心
     */
    synchronized void initConfigCenter() {
        ConfigClientSetting setting = ConfigClientSetting.builder()
                .host(host)
                .port(port)
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .env(env)
                .machine(MachineUtils.getHostName())
                .securityKey(securityKey)
                .connectorType(connectorType)
                .runtimeMode(runtimeMode)
                .workHome(workHome)
                .fileEncoding(fileEncoding)
                .fetchIntervalSeconds(fetchIntervalSeconds)
                .printLog(verbose)
                .build();
        this.configClient = ConfigClient.getInstance();
        this.configClient.init(setting);
    }

    /**
     * 检查参数
     */
    void verification() {

        if (isBlank(host)) {
            log.warn("host:{} is blank, so to set default domain!", host);
            host = "localhost";
        }
        if (isBlank(groupId)) {
            throw new IllegalArgumentException("please set groupId!");
        }
        if (isBlank(artifactId)) {
            throw new IllegalArgumentException("please set artifactId!");
        }
        if (isBlank(version)) {
            log.warn("version:{} is blank, so to set default version!", version);
            version = "1.0.0";
        }
        if (isBlank(env)) {
            log.warn("profile:{} is blank, so to set default profile!", env);
            env = "release";
        }
        if (fetchIntervalSeconds < 1) {
            fetchIntervalSeconds = 60;
            log.warn("fetchIntervalSeconds:{} < 1, so to set default fetchIntervalSeconds 60s!", fetchIntervalSeconds);
        }
        if (runtimeMode == null) {
            log.warn("runtimeMode:{} is blank, so to set default runtimeMode!", runtimeMode);
            runtimeMode = RuntimeMode.REMOTE;
        }
    }

    boolean isBlank(String val) {
        return val == null || val.isEmpty();
    }

    @Override
    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
        super.setFileEncoding(this.fileEncoding);
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 获取占位符对应的值
     *
     * @param placeholder 占位符
     * @param defVal      默认值
     * @return 值
     */
    public String getPlaceholderProperty(String placeholder, String defVal) {
        String value = helper.replacePlaceholders(placeholder, runtimeProperties);
        if (value == null) {
            value = defVal;
        }
        return value;
    }

    /**
     * 获取属性值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public <T> T getProperty(String key, String defaultValue, Class<T> dataType) {
        if (runtimeMode == RuntimeMode.REMOTE) {
            return this.configClient.getProperty(key, defaultValue, dataType);
        }else{
            return ValueUtils.convert(runtimeProperties.getProperty(key, defaultValue), dataType);
        }
    }

    /**
     * 获取属性值
     *
     * @param key 键
     * @return 值
     */
    public String getProperty(String key) {
        return getProperty(key, null, String.class);
    }

    /**
     * 打开文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 输入流
     * @throws IOException 异常
     */
    public InputStream openFile(String filePath, String fileName) throws IOException {
        if (runtimeMode == RuntimeMode.LOCAL) {
            ByteBuf byteBuf = ByteBuf.allocate(1024).autoExpand(true);
            FileInputStream fis = new FileInputStream(this.workHome + "/" + filePath + "/" + fileName);
            try {
                byteBuf.read(fis);
            } finally {
                IOUtils.closeQuietly(fis);
            }
            return byteBuf.asInputStream();
        } else {
            return this.configClient.openFile(filePath, fileName);
        }
    }

    /**
     * 打开文件
     *
     * @param fileName 文件名
     * @return 输入流
     * @throws IOException 异常
     */
    public InputStream openFile(String fileName) throws IOException {
        if (runtimeMode == RuntimeMode.LOCAL) {
            ByteBuf byteBuf = ByteBuf.allocate(1024).autoExpand(true);
            FileInputStream fis = new FileInputStream(this.workHome + "/" + fileName);
            try {
                byteBuf.read(fis);
            } finally {
                IOUtils.closeQuietly(fis);
            }
            return byteBuf.asInputStream();
        } else {
            return this.configClient.openFile(fileName);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        super.setBeanFactory(beanFactory);
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Properties getProperties() {
        if (runtimeMode == RuntimeMode.REMOTE) {
            return this.configClient.getProperties();
        }else{
            return runtimeProperties;
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            postProcessBeanFactory((ConfigurableListableBeanFactory) beanFactory);
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DynamicConfigAspectJ.class);
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition("dynamicConfigAspectJ", builder.getBeanDefinition());
    }
}
