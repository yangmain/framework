package com.rnkrsoft.framework.messagequeue.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 * 消息包装类
 */
@ToString
public class Message<T> {
    @Getter
    @Setter
    String routingKey;
    /**
     * 值对象
     */
    transient T value;
    @Setter
    String json;
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
        this.json = GSON.toJson(value);
    }

    public static Message message(String json) {
        Message message = GSON.fromJson(json, Message.class);
        return message;
    }

    public String asJson() {
        return GSON.toJson(this);
    }

    public T get() {
        if (this.value != null) {
            return value;
        } else {
            Class clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw ErrorContextFactory.instance()
                        .message("消息对应的类对象‘{}’不存在", className)
                        .solution("请确认所在项目是否存在类'{}'", className)
                        .cause(e)
                        .runtimeException();
            }
            Object object = GSON.fromJson(json, clazz);
            this.value = (T) object;
            return (T) object;
        }
    }
}
