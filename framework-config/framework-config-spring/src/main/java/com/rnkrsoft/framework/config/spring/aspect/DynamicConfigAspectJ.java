package com.rnkrsoft.framework.config.spring.aspect;

import com.rnkrsoft.framework.config.utils.ValueUtils;
import com.rnkrsoft.message.MessageFormatter;
import com.rnkrsoft.utils.StringUtils;
import com.rnkrsoft.framework.config.annotation.DynamicFile;
import com.rnkrsoft.framework.config.annotation.DynamicParam;
import com.rnkrsoft.framework.config.spring.DynamicConfigSourceConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2018/2/24.
 * 10:44
 */
@Aspect
@Slf4j
@Order(1)
public class DynamicConfigAspectJ implements ApplicationContextAware {
    PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
    ApplicationContext applicationContext;

    @Pointcut("@within(com.rnkrsoft.framework.config.annotation.DynamicConfig) && execution(public java.io.InputStream+ *..config.*.open*(..))")
    public void openMethod(){
    }
    /**
     * 定义切点，放在config包下的所有对象getter方法
     */
    @Pointcut("@within(com.rnkrsoft.framework.config.annotation.DynamicConfig) && (execution(public * *..config.*.get*()) || execution(public * *..config.*.is*()))")
    public void anyGetterMethod(){
    }
    /**
     * 定义切点，包含有@Value注解的Getter
     *
     * @param joinPoint 切点
     * @return 返回值
     * @throws Throwable 异常
     */
    @Around("anyGetterMethod()")
    public Object getParam(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Class clazz = ms.getDeclaringType();
        Method method = ms.getMethod();
        Value value = method.getAnnotation(Value.class);
        DynamicParam dynamicParam = method.getAnnotation(DynamicParam.class);
        DynamicConfigSourceConfigurer configurer = applicationContext.getBean(DynamicConfigSourceConfigurer.class);
        Field field = null;
        try {
            if (value == null) {
                String prefix = "get";
                if(method.getReturnType() == Boolean.TYPE){
                    prefix = "is";
                }
                String methodName = method.getName();
                String fieldName = null;
                if (methodName.startsWith(prefix)) {
                    fieldName = StringUtils.firstCharToLower(methodName.substring(prefix.length()));
                    if (configurer.getProperty("system.config.log.print", "false", Boolean.class)) {
                        log.info("getter method '{}' has not @Value,so lookup field '{}'!", methodName, fieldName);
                    }
                }
                field = clazz.getDeclaredField(fieldName);
                value = field.getAnnotation(Value.class);
                dynamicParam = field.getAnnotation(DynamicParam.class);
            }
        } catch (Exception e) {

        }
        if (dynamicParam == null && value == null) {
            if (configurer.getProperty("system.config.log.print", "false", Boolean.class)) {
                log.info("getter method has not @Value,@DynamicParam,and field has not @Value,@DynamicParam!");
            }
            return joinPoint.proceed(joinPoint.getArgs());
        }

        if (dynamicParam != null && value != null) {
            if (configurer.getProperty("system.config.log.print", "false", Boolean.class)) {
                log.info("illegal annotation, field '{}' at same time use @DynamicValue and @Value");
            }
            throw new IllegalArgumentException("illegal annotation, field '{}' at same time use @DynamicValue and @Value");
        }

        String key = null;
        if (dynamicParam != null) {
            key = dynamicParam.value();
        } else if (value != null) {
            key = value.value();
        }

        String remoteVal = null;
        Properties properties = configurer.getProperties();
        try {
            if(key.isEmpty()){
                key = clazz.getName() + "." + field.getName();
                remoteVal = properties.getProperty(key);
            }else{
                remoteVal = placeholderHelper.replacePlaceholders(key, properties);
            }
            Object val = convert(method, remoteVal);
            field.setAccessible(true);
            field.set(joinPoint.getThis(), val);
            return val;
        } catch (Exception e) {
            log.error(MessageFormatter.format("get local or remote param value happens error, key : '{}'", key), e);
            log.error("===========begin================get local or remote param value happens error,all params=============================================");
            for (String paramName : properties.stringPropertyNames()){
                log.error("---->{}:{}", paramName, properties.getProperty(paramName));
            }
            log.error("============end===============get local or remote param value happens error,all params=============================================");
            //如果远程不存在值，说明远程没有，则使用本地的
            Object oldValue = joinPoint.proceed();
            return oldValue;
        }

    }
    /**
     * 定义切点，包含有@Value注解的Getter
     * @param joinPoint 切点
     * @return 返回值
     * @throws Throwable 异常
     */
    @Around("openMethod() && @annotation(com.rnkrsoft.framework.config.annotation.DynamicFile)")
    public Object openFile(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        DynamicFile dynamicFile = method.getAnnotation(DynamicFile.class);
        DynamicConfigSourceConfigurer configurer = applicationContext.getBean(DynamicConfigSourceConfigurer.class);
        Object is = null;
        try{
            is = configurer.openFile(dynamicFile.value());
        }catch (IOException e){
            throw e;
        }
        return is;
    }

    Object convert(Method method, String remoteVal){
        return ValueUtils.convert(remoteVal, method.getReturnType());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}