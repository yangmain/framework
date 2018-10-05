package com.rnkrsoft.framework.cache.serialize;


/**
 * 序列化标准
 *
 * @author Hezf
 */
public interface Serializer<T> {
    /**
     * 把一个对象序列化为byte数组
     *
     * @param obj 对象
     * @return byte[]
     */
    public byte[] serialize(T obj);

}
