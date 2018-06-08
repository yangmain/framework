package com.rnkrsoft.framework.orm.mongo.proxy;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.mongo.spring.MongoDaoSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by rnkrsoft.com on 2018/5/27.
 */
@Slf4j
public class MongoProxy<MongodbDAO> implements InvocationHandler {
    Class<MongodbDAO> daoClass;
    Class entityClass;
    MongoDaoSupport mongoDaoSupport;

    public MongoProxy(MongoDaoSupport mongoDaoSupport) {
        this.mongoDaoSupport = mongoDaoSupport;
        this.entityClass = extractEntityClass(daoClass);
    }


    /**
     * 提取接口声明的实体类对象
     *
     * @param mapperClass 映射类
     * @return
     */
    public static Class extractEntityClass(Class mapperClass) {
        //获取当前Mapper实现的接口
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            //检查泛型
            if (type instanceof ParameterizedType) {
                //mapper类
                ParameterizedType target = (ParameterizedType) type;
                Type[] parameters = target.getActualTypeArguments();
                Class<?> modelClass = (Class<?>) parameters[0];
                return modelClass;
            }
        }
        throw new IllegalArgumentException("没有定义实体泛型");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals(Constants.INSERT)) {
            if (args.length != 1) {

            }
            mongoDaoSupport.insert(args[0]);
            return null;
        } else if (methodName.equals(Constants.INSERT_SELECTIVE)) {
            if (args.length != 1) {

            }
            mongoDaoSupport.insertSelective(args[0]);
            return null;
        } else if (methodName.equals(Constants.DELETE_AND)) {
            if (args.length != 1) {

            }
            mongoDaoSupport.delete(args[0]);
        } else if (methodName.equals(Constants.DELETE_OR)) {
            if (args.length != 1) {

            }
            mongoDaoSupport.delete(args[0]);
        } else if (methodName.equals(Constants.DELETE_BY_PRIMARY_KEY)) {
            if (args.length != 1) {

            }
            mongoDaoSupport.deleteByPrimaryKey(args[0]);
        } else if (methodName.equals(Constants.UPDATE_BY_PRIMARY_KEY)) {
            if (args.length != 1) {

            }
            mongoDaoSupport.updateByPrimaryKey(null, null);
        } else if (methodName.equals(Constants.UPDATE_BY_PRIMARY_KEY_SELECTIVE)) {
            if (args.length != 1) {

            }
            mongoDaoSupport.updateByPrimaryKeySelective(null, null);
        } else if (methodName.equals(Constants.SELECT_AND)) {
            if (args.length != 1) {

            }
        } else if (methodName.equals(Constants.SELECT_OR)) {
            if (args.length != 1) {

            }
        } else if (methodName.equals(Constants.SELECT_RUNTIME)) {
            if (args.length != 1) {

            }
            log.debug("selectRuntime");
        } else if (methodName.equals(Constants.SELECT_BY_PRIMARY_KEY)) {
            if (args.length != 1) {

            }
        }
        return null;
    }


}
