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

        public MongoProxyFactory(Class<MongodbDAO> mongoInterface, MongoDaoClient mongoDaoSupport) {
            this.mongoDaoSupport = mongoDaoSupport;
            this.mongoInterface = mongoInterface;
            if (!Arrays.asList(mongoInterface.getInterfaces()).contains(MongoMapper.class)){
                throw ErrorContextFactory.instance().message("接口 '{}'不能作为Mongodb 数据访问对象", mongoInterface).runtimeException();
            }
        }

    public MongodbDAO newInstance() {
        MongoProxy<MongodbDAO> mongoProxy = new MongoProxy<MongodbDAO>(mongoDaoSupport);
        return (MongodbDAO) Proxy.newProxyInstance(mongoInterface.getClassLoader(), new Class[]{mongoInterface}, mongoProxy);
    }

}
