package com.rnkrsoft.framework.orm.mongo.spring;

import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import com.rnkrsoft.framework.orm.mongo.proxy.MongoProxyFactory;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by rnkrsoft.com on 2018/6/27.
 */
public class MongoMapperFactoryBean<T> implements InitializingBean, FactoryBean<T> {
    @Setter
    Class<T> mongoInterface;

    @Setter
    MongoDaoClient mongoDaoClient;

    @Override
    public T getObject() throws Exception {
        MongoProxyFactory<T> mongoProxyFactory = new MongoProxyFactory(mongoInterface, mongoDaoClient);
        return mongoProxyFactory.newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return mongoInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
