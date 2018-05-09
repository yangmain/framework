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
        ConfigClient configClient = ConfigClient.getInstance();
        ConfigClientSetting setting = ConfigClientSetting.builder()
                .groupId("com.rnkrsoft.framework")
                .artifactId("config")
                .version("1.0.0")
                .env("test")
                .machine("test1")
                .desc("测试")
                .fileEncoding("UTF-8")
                .connectorType(ConnectorType.HTTP)
                .runtimeMode(RuntimeMode.LOCAL)
                .fetchDelaySeconds(1)
                .fetchIntervalSeconds(2)
                .build();
        configClient.init(setting);
        Thread.sleep(10 * 1000);
    }
}