package com.rnkrsoft.framework.cache.serialize;

/**
 * 反序列化标准
 *
 * @author Hezf
 */
public interface Deserializer<T> {
    /**
     * 把byte[]数组反序列化为对象
     *
     * @param bs byte数组
     * @return Object
     */
    public T deserialize(byte[] bs);
}
