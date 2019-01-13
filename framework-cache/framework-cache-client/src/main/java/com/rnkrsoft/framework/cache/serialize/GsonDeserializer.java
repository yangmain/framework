package com.rnkrsoft.framework.cache.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.cache.client.redis.Wrapper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rnkrsoft.com on 2017/5/17.
 */
public class GsonDeserializer implements Deserializer<Object> {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    static final Map<String, Class> CACHE = new HashMap();

    @Override
    public Object deserialize(byte[] bs) {
        String json = null;
        try {
            json = new String(bs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            Wrapper objectWrapper = Wrapper.valueOf(json);
            return objectWrapper.get();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
