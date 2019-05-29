package com.rnkrsoft.framework.config.connector;

import com.rnkrsoft.framework.config.utils.MachineUtils;
import com.rnkrsoft.framework.config.v1.*;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
public class HttpConnectorTest {

    @Test
    public void testFetch() throws Exception {
        Connector connector = new HttpConnector("configure.pre.evpopwork.com", 8090);
        connector.start();
        FetchRequest request = FetchRequest.builder()
                .id(UUID.randomUUID().toString())
                .groupId("com.rnkrsoft")
                .artifactId("framework-config-client")
                .version("1.0.0")
                .env("DEV")
                .machine(MachineUtils.getHostName())
                .mac("ssssssssssssssssssss")
                .build();
        FetchResponse response = connector.fetch(request);
        System.out.println(response);
        connector.stop();
    }

    @Test
    public void testGetParam() throws Exception {
        Connector connector = new HttpConnector("localhost", 8080);
        connector.start();
        GetParamRequest request = GetParamRequest.builder()
                .id(UUID.randomUUID().toString())
                .groupId("com.rnkrsoft")
                .artifactId("framework-config-client")
                .version("1.0.0")
                .env("DEV")
                .machine(MachineUtils.getHostName())
                .build();
        request.getKeys().add("param1");
        GetParamResponse response = connector.getParam(request);
        System.out.println(response);
        connector.stop();
    }
    @Test
    public void testPush() throws Exception {
        Connector connector = new HttpConnector("localhost", 8080);
        connector.start();
        PushRequest request = PushRequest.builder()
                .groupId("com.rnkrsoft")
                .artifactId("framework-config-client")
                .version("1.0.0")
                .machine(MachineUtils.getHostName())
                .mac("ssssssssssssssssssss")
                .userId("demo")
                .build();
        request.getParams().add(ParamObject.builder()
                .key("devMode")
                .value("true")
                .encrypt(true)
                .enabled(true)
                .systemProperties(true)
                .dynamic(true)
                .createTime("2019/05/20 00:00:00")
                .updateTime("2019/05/20 00:00:00")
                .build());
        PushResponse response = connector.push(request);
        System.out.println(response);
    }
}