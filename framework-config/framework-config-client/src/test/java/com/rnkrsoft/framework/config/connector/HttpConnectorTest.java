package com.rnkrsoft.framework.config.connector;

import com.rnkrsoft.framework.config.v1.FetchRequest;
import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.PushRequest;
import com.rnkrsoft.framework.config.v1.PushResponse;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
public class HttpConnectorTest {

    @Test
    public void testFetch() throws Exception {
        Connector connector = new HttpConnector("localhost", 80);
        connector.start();
        FetchRequest request = FetchRequest.builder()
                .groupId("com.rnkrsoft")
                .artifactId("config")
                .version("1.0.0")
                .env("test")
                .machine("test1")
                .mac("ssssssssssssssssssss")
                .build();
        FetchResponse response = connector.fetch(request);
        connector.stop();
    }

    @Test
    public void testPush() throws Exception {
        Connector connector = new HttpConnector("localhost", 80);
        connector.start();
        PushRequest request = PushRequest.builder()
                .groupId("com.rnkrsoft")
                .artifactId("config")
                .version("1.0.0")
                .env("test")
                .machine("test1")
                .mac("ssssssssssssssssssss")
                .build();
        PushResponse response = connector.push(request);
    }
}