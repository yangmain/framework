package com.rnkrsoft.framework.config.client;

import com.rnkrsoft.framework.config.v1.RuntimeMode;
import com.rnkrsoft.framework.config.v1.ConnectorType;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
public class ConfigClientTest {
    @Test
    public void testFetch() throws InterruptedException {
        final ConfigClient configClient = ConfigClient.getInstance();
        ConfigClientSetting setting = ConfigClientSetting.builder()
                .host("localhost")
                .port(8080)
                .groupId("com.rnkrsoft")
                .artifactId("framework-config-client")
                .version("1.0.0")
                .env("DEV")
                .machine("m1")
                .desc("测试")
                .fileEncoding("UTF-8")
                .connectorType(ConnectorType.HTTP)
                .runtimeMode(RuntimeMode.REMOTE)
                .securityKey("dcdac968-8777-48e2-a4a5-ac3c29c6dd50")
                .fetchIntervalSeconds(2)
                .build();
        configClient.init(setting);


        Thread t = new Thread(){
            @Override
            public void run() {
                while (true){
                    System.out.println(configClient.getProperty("log", null, String.class));
                    System.out.println(configClient.getProperty("param1", null, String.class));
                    System.out.println(configClient.getProperty("param2", null, String.class));
                    System.out.println(configClient.getProperty("param3", null, String.class));
                    System.out.println(configClient.getProperty("param4", null, String.class));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

        Thread.sleep(30* 1000);
    }
}