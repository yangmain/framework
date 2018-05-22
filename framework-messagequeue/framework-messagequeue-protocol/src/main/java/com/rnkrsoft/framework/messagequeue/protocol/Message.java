package com.rnkrsoft.framework.messagequeue.protocol;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.UnsupportedEncodingException;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 * 消息包装类
 */
public class Message<T>{
    /**
     * 值对象
     */
    volatile T value;
    /**
     * 类名
     */
    @Getter
    String className;
    /**
     * 字节数组数据
     */
    @Getter
    byte[] data;
    /**
     * 消息年龄
     */
    @Getter
    int age = 0;
    /**
     * 创建日期
     */
    @Getter
    long createDate;
    /**
     * 上次更新日期
     */
    @Getter
    long lastUpdateDate;
    static Gson GSON = new GsonBuilder().serializeNulls().create();


    public Message(T value) {
        this.value = value;
        this.age = 0;
        this.createDate = System.currentTimeMillis();
        this.lastUpdateDate = System.currentTimeMillis();
    }

    public T get(){
        return get("UTF-8");
    }

    public String asString(String encoding){
        String json = null;
        try {
            json = new String(data, encoding);
        } catch (UnsupportedEncodingException e) {
            throw ErrorContextFactory.instance().cause(e).runtimeException();
        }
        return json;
    }

    public T get(String encoding){
        if (this.value != null) {
            return value;
        }else{
            String json = asString(encoding);
            Class clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw ErrorContextFactory.instance().cause(e).runtimeException();
            }
            Object object = GSON.fromJson(json, clazz);
            return (T) object;
        }
    }


}
