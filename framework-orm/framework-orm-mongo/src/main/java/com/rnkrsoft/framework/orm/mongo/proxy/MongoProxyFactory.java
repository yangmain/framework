package com.rnkrsoft.framework.orm.mongo.proxy;

import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.orm.mongo.MongoMapper;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
public class MongoProxyFactory<MongodbDAO>{
        final Class<MongodbDAO> mongoInterface;
        final MongoDaoClient mongoDaoSupport;
        final  Class entityClass;

        public MongoProxyFactory(Class<MongodbDAO> mongoInterface, Class entityClass,  MongoDaoClient mongoDaoSupport) {
            this.mongoDaoSupport = mongoDaoSupport;
            this.mongoInterface = mongoInterface;
            this.entityClass = entityClass;
            if (!Arrays.asList(mongoInterface.getInterfaces()).contains(MongoMapper.class)){
                throw ErrorContextFactory.instance().message("接口 '{}'不能作为Mongodb 数据访问对象", mongoInterface).runtimeException();
            }
        }

    public MongodbDAO newInstance() {
        MongoProxy<MongodbDAO> mongoProxy = new MongoProxy<MongodbDAO>(this.mongoDaoSupport, this.entityClass);
        return (MongodbDAO) Proxy.newProxyInstance(mongoInterface.getClassLoader(), new Class[]{mongoInterface}, mongoProxy);
    }

}
