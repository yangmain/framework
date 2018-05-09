package com.rnkrsoft.framework.config.spring;

import com.devops4j.io.buffer.ByteBuf;
import com.devops4j.message.MessageFormatter;
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
     * 拉取参数延时秒数
     */
    @Setter
    long fetchDelaySeconds = 60;
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
        }
        String ip = "localhost";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取本机IP地址发生错误");
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
                        log.info("load client's config [groupId, artifactId , version, env] -> [{}, {}, {}, {}]", groupId, artifactId, version, env);
                        //初始化配置中心客户端
                        initConfigCenter();
                        //拉取配置
                        this.configClient.fetch();
                        //转换属性
                        convertProperties(this.runtimeProperties);
                    }
                    PropertySource<?> localPropertySource = new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, this.runtimeProperties);
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
                .connectorType(connectorType)
                .runtimeMode(runtimeMode)
                .workHome(workHome)
                .fileEncoding(fileEncoding)
                .fetchIntervalSeconds(fetchIntervalSeconds)
                .fetchDelaySeconds(fetchDelaySeconds)
                .build();
        this.configClient = ConfigClient.getInstance();
        this.configClient.init(setting);
    }

    public void scan(Class clazz, Map<String, ParamObject> params, Map<String, FileObject> files) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            DynamicParam dynamicParam = field.getAnnotation(DynamicParam.class);
            if (dynamicParam == null) {
                continue;
            }
            String placeholderKey = dynamicParam.value();
            String key = null;
            String value = null;
            boolean sysProp = dynamicParam.systemProperty();
            if (placeholderKey.isEmpty()) {
                key = clazz.getName() + "." + field.getName();
            } else {
                int prefixIdx = placeholderKey.indexOf(placeholderPrefix);
                int suffixIdx = placeholderKey.indexOf(placeholderSuffix);
                if (prefixIdx > -1 && suffixIdx > -1) {
                    int valueSeparatorIdx = placeholderKey.lastIndexOf(valueSeparator);
                    key = placeholderKey.substring(prefixIdx + placeholderPrefix.length(), suffixIdx);
                    value = null;
                    if (valueSeparatorIdx > -1) {
                        key = placeholderKey.substring(prefixIdx + placeholderPrefix.length(), valueSeparatorIdx);
                        value = placeholderKey.substring(valueSeparatorIdx + 1, suffixIdx);
                    }
                } else {
                    throw new IllegalArgumentException(MessageFormatter.format("class：{} field :{} has placeholder '{}' is illegal!", clazz, field, placeholderKey));
                }

            }

            ParamObject paramItem = params.get(key);
            if (paramItem == null) {
                paramItem = new ParamObject();
                paramItem.setKey(key);
                paramItem.setValue("");
                params.put(key, paramItem);
            }
            ParamType type = dynamicParam.type();
            String desc = dynamicParam.desc();
            if (value != null) {
                paramItem.setValue(value);
            }
            paramItem.setType(type.getCode());
            paramItem.setSystemProperties(sysProp);
            paramItem.setDesc(desc);
            paramItem.setDynamic(true);
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            DynamicFile dynamicFile = method.getAnnotation(DynamicFile.class);
            if (dynamicFile == null || dynamicFile.value() == null) {
                continue;
            }
            FileObject fileItem = files.get(dynamicFile.value());
            if (fileItem == null) {
                fileItem = new FileObject();
                String file = FileSystemUtils.formatPath(dynamicFile.value());
                int fileIdx = file.lastIndexOf("/");
                String filePath = file.substring(0, fileIdx);
                String fileName = file.substring(fileIdx + 1);
                fileItem.setFilePath(filePath);
                fileItem.setFileName(fileName);
                files.put(file, fileItem);
            }
            fileItem.setEnabled(true);
            fileItem.setCreateUid(MachineUtils.getHostName());
            fileItem.setLastUpdateUid(MachineUtils.getHostName());
            fileItem.setLazyDownload(false);
            fileItem.setTransferType(FileTransferType.HTTP);
        }
    }

    /**
     * 检查参数
     */
    void verification() {

        if (isBlank(host)) {
            log.warn("host:{} is blank, so to set defalut domain!", host);
            host = "localhost";
        }
        if (isBlank(groupId)) {
            throw new IllegalArgumentException("please set groupId!");
        }
        if (isBlank(artifactId)) {
            throw new IllegalArgumentException("please set artifactId!");
        }
        if (isBlank(version)) {
            log.warn("version:{} is blank, so to set defalut version!", version);
            version = "1.0.0";
        }
        if (isBlank(env)) {
            log.warn("profile:{} is blank, so to set defalut profile!", env);
            env = "release";
        }
        if (fetchDelaySeconds < 1) {
            fetchDelaySeconds = 60;
            log.warn("fetchDelaySeconds:{} < 1, so to set defalut fetchDelaySeconds 60s!", fetchDelaySeconds);
        }
        if (fetchIntervalSeconds < 1) {
            fetchIntervalSeconds = 60;
            log.warn("fetchIntervalSeconds:{} < 1, so to set defalut fetchIntervalSeconds 60s!", fetchIntervalSeconds);
        }
        if (runtimeMode == null) {
            log.warn("runtimeMode:{} is blank, so to set defalut runtimeMode!", runtimeMode);
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

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
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
    public String getProperty(String key, String defaultValue) {
        return runtimeProperties.getProperty(key, defaultValue);
    }

    /**
     * 获取属性值
     *
     * @param key 键
     * @return 值
     */
    public String getProperty(String key) {
        return runtimeProperties.getProperty(key);
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
        return runtimeProperties;
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
