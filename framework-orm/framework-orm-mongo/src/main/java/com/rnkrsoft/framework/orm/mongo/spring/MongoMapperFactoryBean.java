package com.rnkrsoft.framework.orm.mongo.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by woate on 2018/6/27.
 */
public class MongoMapperFactoryBean<T> implements InitializingBean, FactoryBean<T> {
    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
