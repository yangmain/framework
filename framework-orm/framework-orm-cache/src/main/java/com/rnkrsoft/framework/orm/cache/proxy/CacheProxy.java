package com.rnkrsoft.framework.orm.cache.proxy;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.orm.cache.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by rnkrsoft.com on 2018/6/2.
 */
public class CacheProxy<CacheDAO> implements InvocationHandler {
    CachedMap<String, Object> cachedMap;

    public CacheProxy(CachedMap<String, Object> cachedMap) {
        this.cachedMap = cachedMap;
    }

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {
        Metadata metadata = metadata(method);
        if (metadata.commandType == CommandType.DECR){
            String key = args[0].toString();
            return cachedMap.decr(key);
        }else if (metadata.commandType == CommandType.EXPIRE){
            String key = args[0].toString();
            int second = Integer.parseInt(args[1].toString());
            cachedMap.expire(key, second);
            return null;
        }else if (metadata.commandType == CommandType.GET){
            String key = args[0].toString();
            return cachedMap.get(key);
        }else if(metadata.commandType == CommandType.GETSET){
            String key = args[0].toString();
            Object value = args[1];
            return cachedMap.put(key, value, metadata.expire);
        }else if(metadata.commandType == CommandType.INCR){
            String key = args[0].toString();
            return cachedMap.incr(key);
        }else if(metadata.commandType == CommandType.KEYS){
            String pattern = args[0].toString();
            return cachedMap.keys(pattern);
        }else if(metadata.commandType == CommandType.PRESIST){
            String key = args[0].toString();
            cachedMap.presist(key);
            return null;
        }else if(metadata.commandType == CommandType.SET){
            String key = args[0].toString();
            Object value = args[1];
            cachedMap.put(key, value, metadata.seconds);
            return null;
        }else if(metadata.commandType == CommandType.TTL){
            String key = args[0].toString();
            return cachedMap.ttl(key);
        }else if(metadata.commandType == CommandType.TYPE){
            String key = args[0].toString();
            return cachedMap.getNativeClass(key);
        }else if(metadata.commandType == CommandType.CACHED_MAP){
            return cachedMap;
        }else{
            return null;
        }
    }

    Metadata metadata(Method method){
        Metadata metadata = new Metadata();
        Decr decr = method.getAnnotation(Decr.class);
        Expire expire = method.getAnnotation(Expire.class);
        Get get = method.getAnnotation(Get.class);
        GetSet getSet = method.getAnnotation(GetSet.class);
        Incr incr = method.getAnnotation(Incr.class);
        Keys keys = method.getAnnotation(Keys.class);
        Presist presist = method.getAnnotation(Presist.class);
        Set set = method.getAnnotation(Set.class);
        Ttl ttl = method.getAnnotation(Ttl.class);
        Type type = method.getAnnotation(Type.class);
        int cnt = 0;
        if (decr != null){
            cnt++;
            metadata.commandType = CommandType.DECR;
            metadata.decrement = decr.decrement();
        }

        if (expire != null){
            cnt++;
            metadata.commandType = CommandType.EXPIRE;
        }
        if (get != null){
            cnt++;
            metadata.commandType = CommandType.GET;
        }

        if (getSet != null){
            cnt++;
            metadata.commandType = CommandType.GETSET;
            metadata.expire = getSet.expire();
        }

        if (incr != null){
            cnt++;
            metadata.commandType = CommandType.INCR;
            metadata.increment = incr.increment();
        }

        if (keys != null){
            cnt++;
            metadata.commandType = CommandType.KEYS;
        }

        if (presist != null){
            cnt++;
            metadata.commandType = CommandType.PRESIST;
        }

        if (set != null){
            cnt++;
            metadata.commandType = CommandType.SET;
            metadata.expire = set.expire();
        }

        if (ttl != null){
            cnt++;
            metadata.commandType = CommandType.TTL;
        }

        if (type != null){
            cnt++;
            metadata.commandType = CommandType.TYPE;
        }
        if (method.getReturnType().isAssignableFrom(CachedMap.class)){
           if (method.getParameterTypes().length != 0){
               throw ErrorContextFactory.instance().message("不支持的类型").runtimeException();
           }else{
               metadata.commandType = CommandType.CACHED_MAP;
               return metadata;
           }
        }
        if (cnt == 0){
            throw ErrorContextFactory.instance().message("未标注注解").runtimeException();
        }
        if (cnt > 1){
            throw ErrorContextFactory.instance().message("不能同时标注注解").runtimeException();
        }
        return metadata;
    }

}
