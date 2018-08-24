package com.rnkrsoft.framework.orm.mongo.proxy;

import com.rnkrsoft.framework.orm.Constants;
import com.rnkrsoft.framework.orm.LogicMode;
import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.framework.orm.mongo.client.MongoDaoClient;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by rnkrsoft.com on 2018/5/27.
 */
@Slf4j
public class MongoProxy<MongodbDAO> implements InvocationHandler {
    Class entityClass;
    MongoDaoClient mongoDaoClient;

    public MongoProxy(MongoDaoClient mongoDaoClient, Class entityClass) {
        this.mongoDaoClient = mongoDaoClient;
        this.entityClass = entityClass;
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
                throw ErrorContextFactory.instance().message("insert 不支持不为一个参数的").runtimeException();
            }
            mongoDaoClient.insert(new Object[]{args[0]});
            return 0;
        } else if (methodName.equals(Constants.INSERT_SELECTIVE)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("insertSelective 不支持不为一个参数的").runtimeException();
            }
            mongoDaoClient.insertSelective(args[0]);
            return 0;
        } else if (methodName.equals(Constants.DELETE)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("deleteAnd 不支持不为一个参数的").runtimeException();
            }
            return mongoDaoClient.delete(args[0]);
        } else if (methodName.equals(Constants.DELETE_BY_PRIMARY_KEY)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("deleteByPrimaryKey 不支持不为一个参数的").runtimeException();
            }
           return mongoDaoClient.deleteByPrimaryKey(args[0]);
        } else if (methodName.equals(Constants.UPDATE)) {
            if (args.length != 2) {
                throw ErrorContextFactory.instance().message("updateByPrimaryKey 不支持不为两个参数的").runtimeException();
            }
            return mongoDaoClient.update(args[0], args[1]);
        } else if (methodName.equals(Constants.UPDATE_SELECTIVE)) {
            if (args.length != 2) {
                throw ErrorContextFactory.instance().message("updateByPrimaryKey 不支持不为两个参数的").runtimeException();
            }
            return mongoDaoClient.updateSelective(args[0], args[1]);
        } else if (methodName.equals(Constants.UPDATE_BY_PRIMARY_KEY)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("updateByPrimaryKey 不支持不为一个参数的").runtimeException();
            }
            return mongoDaoClient.updateByPrimaryKey(null, null);
        } else if (methodName.equals(Constants.UPDATE_BY_PRIMARY_KEY_SELECTIVE)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("updateByPrimaryKeySelective 不支持不为一个参数的").runtimeException();
            }
            return mongoDaoClient.updateByPrimaryKeySelective(null, null);
        } else if (methodName.equals(Constants.SELECT)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("select 不支持不为一个参数的").runtimeException();
            }
            return mongoDaoClient.select(args[0]);
        } else if (methodName.equals(Constants.SELECT_BY_PRIMARY_KEY)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("selectByPrimaryKey 不支持不为一个参数的").runtimeException();
            }
            return mongoDaoClient.selectByPrimaryKey(args[0]);
        } else if (methodName.equals(Constants.SELECT_PAGE_AND)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("selectByPrimaryKey 不支持不为一个参数的").runtimeException();
            }
            return mongoDaoClient.selectPage((Pagination) args[0], LogicMode.AND);
        } else if (methodName.equals(Constants.SELECT_PAGE_OR)) {
            if (args.length != 1) {
                throw ErrorContextFactory.instance().message("selectByPrimaryKey 不支持不为一个参数的").runtimeException();
            }
            return mongoDaoClient.selectPage((Pagination)args[0], LogicMode.OR);
        }
        return null;
    }


}
