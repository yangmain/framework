package com.rnkrsoft.framework.cache.client.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

public class Wrapper implements Serializable {
    protected static Gson GSON = new GsonBuilder().serializeNulls().setDateFormat("yyyyMMddHHmmssSSS").create();
    /**
     * 序列化版本号
     */
    @Setter
    @Getter
    long serialVersionUID = 1L;
    /**
     * 数据对象类型
     */
    @Getter
    String className;
    /**
     * JSON数据
     */
    @Getter
    String data;

    public Wrapper(Object data) {
        setData(data);
    }

    public void setData(Object data) {
        if (data instanceof String) {
            this.data = (String) data;
        } else {
            this.data = GSON.toJson(data);
        }
        if (data == null){
            this.className = String.class.getName();
        }else {
            this.className = data.getClass().getName();
        }
    }

    public <T> T get() throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        return (T) GSON.fromJson(data, clazz);
    }

    public static Wrapper valueOf(String data) {
        return GSON.fromJson(data, Wrapper.class);
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}