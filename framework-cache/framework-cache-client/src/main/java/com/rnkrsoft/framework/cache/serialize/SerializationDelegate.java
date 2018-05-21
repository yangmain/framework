package com.rnkrsoft.framework.cache.serialize;


import com.devops4j.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2017/5/17.
 * 序列化和反序列化代理
 *
 */
public class SerializationDelegate implements Serializer<Object>, Deserializer<Object> {
    private final Serializer<Object> serializer;
    private final Deserializer<Object> deserializer;

    public SerializationDelegate() {
        this.serializer = new GsonSerializer();
        this.deserializer = new GsonDeserializer();
    }

    public SerializationDelegate(Serializer<Object> serializer, Deserializer<Object> deserializer) {
        if (serializer == null){
            throw ErrorContextFactory.instance().message("Serializer must not be null").runtimeException();
        }
        if (deserializer == null){
            throw ErrorContextFactory.instance().message("Deserializer must not be null").runtimeException();
        }
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public Object deserialize(byte[] bs) {
        return deserializer.deserialize(bs);
    }

    @Override
    public byte[] serialize(Object obj) {
        return serializer.serialize(obj);
    }

}
