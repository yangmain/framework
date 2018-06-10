package com.rnkrsoft.framework.orm.mongo.proxy;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.mongodb.MongoClient;
import com.rnkrsoft.framework.orm.mongo.MongoMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by woate on 2018/6/2.
 */
public class MongoProxyFactory<MongodbDAO>{
        final Class<MongodbDAO> mongoInterface;
        final MongoClient mongoClient;

        public MongoProxyFactory(Class<MongodbDAO> mongoInterface, MongoClient mongoClient) {
            this.mongoClient = mongoClient;
            this.mongoInterface = mongoInterface;
            if (!Arrays.asList(mongoInterface.getInterfaces()).contains(MongoMapper.class)){
                throw ErrorContextFactory.instance().message("接口 '{}'不能作为Mongodb 数据访问对象", mongoInterface).runtimeException();
            }
        }

    public MongodbDAO newInstance() {

        MongoProxy<MongodbDAO> mongoProxy = null;
        return (MongodbDAO) Proxy.newProxyInstance(mongoInterface.getClassLoader(), new Class[]{mongoInterface}, mongoProxy);
    }

}
