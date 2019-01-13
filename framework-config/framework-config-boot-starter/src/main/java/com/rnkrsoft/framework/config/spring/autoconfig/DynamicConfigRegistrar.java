package com.rnkrsoft.framework.config.spring.autoconfig;

import com.rnkrsoft.framework.config.spring.DynamicConfigSourceConfigurer;
import com.rnkrsoft.framework.config.spring.autoconfig.annotation.EnableDynamicConfig;
import com.rnkrsoft.framework.config.v1.RuntimeMode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * Created by rnkrsoft.com on 2018/2/10.
 * 自动注解启用配置中心
 */
public class DynamicConfigRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableDynamicConfig.class.getName(), false);
		String host = (String) attributes.get("host");
		Integer port = (Integer) attributes.get("port");
		String groupId = (String) attributes.get("groupId");
		String artifactId = (String) attributes.get("artifactId");
		String version = (String) attributes.get("version");
		String env = (String) attributes.get("env");
		Integer fetchDelaySeconds = (Integer) attributes.get("fetchDelaySeconds");
		Integer fetchIntervalSeconds = (Integer) attributes.get("fetchIntervalSeconds");
		RuntimeMode runtimeMode = (RuntimeMode) attributes.get("runtimeMode");
		String workHome = (String) attributes.get("workHome");
		String locations = (String) attributes.get("locations");

	    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DynamicConfigSourceConfigurer.class);
	    builder.setScope(BeanDefinition.SCOPE_SINGLETON);
		builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		builder.addPropertyValue("host", host);
		builder.addPropertyValue("port", port);
		builder.addPropertyValue("groupId", groupId);
		builder.addPropertyValue("artifactId", artifactId);
		builder.addPropertyValue("version", version);
		builder.addPropertyValue("env", env);
		builder.addPropertyValue("fetchDelaySeconds", fetchDelaySeconds);
		builder.addPropertyValue("fetchIntervalSeconds", fetchIntervalSeconds);
		builder.addPropertyValue("runtimeMode", runtimeMode);
		builder.addPropertyValue("locations", locations);
		builder.addPropertyValue("workHome", workHome);
		registry.registerBeanDefinition("dynamicConfigSourceConfigurer", builder.getBeanDefinition());

	}
}
