package com.rnkrsoft.framework.messagequeue.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 * 消息包装类
 */
public class Message<T> {
    @Getter
    @Setter
    String routingKey;
    /**
     * 值对象
     */
    T value;
    /**
     * 类名
     */
    @Getter
    @Setter
    String className;
    /**
     * 消息年龄
     */
    @Getter
    @Setter
    int age = 0;
    /**
     * 创建日期
     */
    @Getter
    @Setter
    long createDate;
    /**
     * 上次更新日期
     */
    @Getter
    @Setter
    long lastUpdateDate;
    static Gson GSON = new GsonBuilder().serializeNulls().create();


    public Message(T value) {
        this.value = value;
        if (this.value != null) {
            this.className = this.value.getClass().getName();
        }
        this.age = 0;
        this.createDate = System.currentTimeMillis();
        this.lastUpdateDate = System.currentTimeMillis();
    }

    public static Message message(String json) {
        Message message = GSON.fromJson(json, Message.class);
        return message;
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
