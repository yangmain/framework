package com.rnkrsoft.framework.orm.cache.proxy;

import com.rnkrsoft.framework.orm.cache.Set;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.cache.client.CachedMap;
import com.rnkrsoft.framework.orm.cache.*;

import java.io.Serializable;
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
        if (metadata.commandType == CommandType.DECR) {
            String key = args[0] == null ? null : args[0].toString();
            Integer decrement = metadata.decrement;
            if (args.length == 2 && args[1] != null) {
                decrement = Integer.valueOf(args[1].toString());
            }
            return cachedMap.decr(key, decrement);
        } else if (metadata.commandType == CommandType.INCR) {
            String key = args[0] == null ? null : args[0].toString();
            Integer increment = metadata.increment;
            if (args.length == 2 && args[1] != null) {
                increment = Integer.valueOf(args[1].toString());
            }
            return cachedMap.incr(key, increment);
        } else if (metadata.commandType == CommandType.EXPIRE) {
            String key = args[0] == null ? null : args[0].toString();
            int expire = Integer.parseInt(args[1].toString());
            if (method.getReturnType() == Void.TYPE) {
                cachedMap.expire(key, expire);
            } else {
                return cachedMap.expire(key, expire);
            }
        } else if (metadata.commandType == CommandType.GET) {
            String key = args[0] == null ? null : args[0].toString();
            return cachedMap.get(key);
        } else if (metadata.commandType == CommandType.GETSET) {
            String key = args[0] == null ? null : args[0].toString();
            Object value = args[1];
            return cachedMap.put(key, value, metadata.expire);
        } else if (metadata.commandType == CommandType.KEYS) {
            String pattern = args[0] == null ? null : args[0].toString();
            return cachedMap.keys(pattern);
        } else if (metadata.commandType == CommandType.PERSIST) {
            String key = args[0] == null ? null : args[0].toString();
            if (method.getReturnType() == Long.TYPE) {
                Long result = cachedMap.persist(key);
                if (result == null) {
                    return 0L;
                }
            } else {
                return cachedMap.persist(key);
            }
        } else if (metadata.commandType == CommandType.SET) {
            String key = args[0] == null ? null : args[0].toString();
            Object value = args[1];
            if (method.getReturnType() == Void.TYPE) {
                cachedMap.put(key, value, metadata.expire);
            } else {
                return cachedMap.put(key, value, metadata.expire);
            }
        } else if (metadata.commandType == CommandType.TTL) {
            String key = args[0] == null ? null : args[0].toString();
            return cachedMap.ttl(key);
        } else if (metadata.commandType == CommandType.TYPE) {
            String key = args[0] == null ? null : args[0].toString();
            return cachedMap.getNativeClass(key);
        } else if (metadata.commandType == CommandType.REMOVE) {
            String key = args[0] == null ? null : args[0].toString();
            if (method.getReturnType() == Void.TYPE) {
                cachedMap.remove(key);
            } else {
                return cachedMap.remove(key);
            }
        } else if (metadata.commandType == CommandType.CACHED_MAP) {
            return cachedMap;
        }
        return null;
    }

    Metadata metadata(Method method) {
        Metadata metadata = new Metadata();
        Incr incr = method.getAnnotation(Incr.class);
        Decr decr = method.getAnnotation(Decr.class);
        Expire expire = method.getAnnotation(Expire.class);
        Get get = method.getAnnotation(Get.class);
        GetSet getSet = method.getAnnotation(GetSet.class);
        Keys keys = method.getAnnotation(Keys.class);
        Persist persist = method.getAnnotation(Persist.class);
        Set set = method.getAnnotation(Set.class);
        Ttl ttl = method.getAnnotation(Ttl.class);
        Type type = method.getAnnotation(Type.class);
        Remove remove = method.getAnnotation(Remove.class);
        int cnt = 0;
        if (incr != null) {
            if ((method.getParameterTypes().length < 1
                    || method.getParameterTypes().length > 2)
                    || (method.getReturnType() != Void.TYPE
                    && method.getReturnType() != Void.class
                    && method.getReturnType() != Long.TYPE
                    && method.getReturnType() != Long.class)) {
                throw ErrorContextFactory.instance()
                        .message("method {}" , method)
                        .solution("参数必须形如（long Long） func(String key [, int increment])")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.INCR;
            metadata.increment = incr.increment();
        }

        if (decr != null) {
            if ((method.getParameterTypes().length < 1
                    || method.getParameterTypes().length > 2)
                    || (method.getReturnType() != Void.TYPE
                    && method.getReturnType() != Void.class
                    && method.getReturnType() != Long.TYPE
                    && method.getReturnType() != Long.class)) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如（long Long） func(String key [, int decrement])")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.DECR;
            metadata.decrement = decr.decrement();
        }

        if (expire != null) {
            if ((method.getParameterTypes().length != 2)
                    || (method.getReturnType() != Void.TYPE
                    && method.getReturnType() != Void.class
                    && method.getReturnType() != Long.TYPE
                    && method.getReturnType() != Long.class)) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 (Void Long) func(String key, int expire)")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.EXPIRE;
        }
        if (get != null) {
            if (method.getParameterTypes().length != 1
                    || !Serializable.class.isAssignableFrom(method.getReturnType())) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 T func(String key)")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.GET;
        }

        if (getSet != null) {
            if ((method.getParameterTypes().length < 1
                    || method.getParameterTypes().length > 2)
                    || !Serializable.class.isAssignableFrom(method.getReturnType())) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 T func(String key[, T value])")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.GETSET;
            metadata.expire = getSet.expire();
        }


        if (keys != null) {
            if (method.getParameterTypes().length != 1
                    || !java.util.Set.class.isAssignableFrom(method.getReturnType())) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 Set<String> func(String key)")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.KEYS;
        }

        if (persist != null) {
            if (method.getParameterTypes().length != 1) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 Long func(String key)")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.PERSIST;
        }

        if (set != null) {
            if (method.getParameterTypes().length != 2
                    || (Void.TYPE.isAssignableFrom(method.getReturnType())
                    && Void.class.isAssignableFrom(method.getReturnType())
                    && !Serializable.class.isAssignableFrom(method.getReturnType()))
                    ) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 [T void] func(String key[, T value])")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.SET;
            metadata.expire = set.expire();
        }

        if (ttl != null) {
            if (method.getParameterTypes().length != 1 || (method.getReturnType() != Long.TYPE && method.getReturnType() != Long.class)) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 Long func(String key)")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.TTL;
        }

        if (type != null) {
            if (method.getParameterTypes().length != 1 || method.getReturnType() != Class.class) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 Class func(String key)")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.TYPE;
        }
        if (remove != null) {
            if (method.getParameterTypes().length != 1 ||
                    (Void.TYPE.isAssignableFrom(method.getReturnType())
                            && Void.class.isAssignableFrom(method.getReturnType())
                            && !Serializable.class.isAssignableFrom(method.getReturnType()))) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("参数必须形如 [T void] func(String key)")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.REMOVE;
        }
        if (method.getReturnType().isAssignableFrom(CachedMap.class)) {
            if (method.getParameterTypes().length != 0) {
                throw ErrorContextFactory.instance()
                        .message("method {}", method)
                        .solution("不支持的类型")
                        .runtimeException();
            }
            cnt++;
            metadata.commandType = CommandType.CACHED_MAP;
            return metadata;
        }
        if (cnt == 0) {
            throw ErrorContextFactory.instance()
                    .message("method {}", method)
                    .solution("未标注注解")
                    .runtimeException();
        }
        if (cnt > 1) {
            throw ErrorContextFactory.instance()
                    .message("method {}", method)
                    .solution("不能同时标注注解")
                    .runtimeException();
        }
        return metadata;
    }

}
