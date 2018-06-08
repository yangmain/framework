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
public class Message<T> {
    @Getter
    String routeKey;
    /**
     * 值对象
     */
    T value;
    /**
     * 类名
     */
    @Getter
    String className;
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

    public static Message message(byte[] data) {
        try {
            Message message = GSON.fromJson(new String(data, "UTF-8"), Message.class);
            return message;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String asJson() {
        String json = null;
        json = GSON.toJson(this);
        return json;
    }

    public T get() {
        if (this.value != null) {
            return value;
        } else {
            String json = asJson();
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
