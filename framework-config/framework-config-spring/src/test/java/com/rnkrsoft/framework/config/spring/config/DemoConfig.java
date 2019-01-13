package com.rnkrsoft.framework.config.spring.config;

import com.rnkrsoft.framework.config.annotation.DynamicConfig;
import com.rnkrsoft.framework.config.annotation.DynamicFile;
import com.rnkrsoft.framework.config.annotation.DynamicParam;
import com.rnkrsoft.framework.config.v1.ParamType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

/**
 * Created by rnkrsoft.com on 2018/2/10.
 * 20:38
 */
@DynamicConfig
@Data
public class DemoConfig {
    @Value(value = "${name:this is a test}")
    String param1;

    @DynamicParam(value = "${param2}", type = ParamType.SYSTEM, encrypt = true, desc = "测试1")
    String param2;

    @DynamicParam(value = "${param3}", type = ParamType.SYSTEM, encrypt = false, desc = "测试2")
    String param3;

    @DynamicParam(type = ParamType.SYSTEM, encrypt = true, desc = "测试4")
    boolean param4;

    @DynamicFile(value = "/demo/demo.txt", encrypt = true, desc = "参数")
    public InputStream openTest() {
        return null;
    }
}