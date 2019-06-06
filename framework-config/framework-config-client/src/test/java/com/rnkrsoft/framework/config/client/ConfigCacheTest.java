package com.rnkrsoft.framework.config.client;

import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.ParamObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConfigCacheTest {

    @Test
    public void save() throws IOException {
        FetchResponse response = new FetchResponse();
        response.getParams().add(ParamObject.builder().key("name").value("12345").build());
        ConfigCache configCache = new ConfigCache(response, ConfigClientSetting.builder()
                .groupId("com.rnkrsoft")
                .artifactId("demo")
                .version("1.0.0")
                .env("DEV")
                .machine("xxxxx")
                .workHome("./target")
                .build());
        configCache.save();
    }

    @Test
    public void load() throws IOException {
        ConfigCache configCache = ConfigCache.load(ConfigClientSetting.builder()
                .groupId("com.rnkrsoft")
                .artifactId("demo")
                .version("1.0.0")
                .env("DEV")
                .machine("xxxxx")
                .workHome("./target")
                .build());
        System.out.println(configCache.getParams());
    }
}