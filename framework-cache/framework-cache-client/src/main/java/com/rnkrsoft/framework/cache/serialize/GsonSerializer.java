package com.rnkrsoft.framework.cache.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.cache.client.redis.Wrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

/**
 * Created by rnkrsoft.com on 2017/5/17.
 */
@Slf4j
public class GsonSerializer implements Serializer{
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    @Override
    public byte[] serialize(Object obj) {
        Wrapper objectWrapper = new Wrapper();
        objectWrapper.setData(gson.toJson(obj));
        objectWrapper.setClassName(obj.getClass().getName());
        String json = gson.toJson(objectWrapper);
        try {
            return json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
