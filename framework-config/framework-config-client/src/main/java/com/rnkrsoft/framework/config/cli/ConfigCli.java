package com.rnkrsoft.framework.config.cli;

import com.rnkrsoft.framework.config.client.ConfigClient;
import com.rnkrsoft.framework.config.client.ConfigClientSetting;
import com.rnkrsoft.framework.config.utils.MachineUtils;
import com.rnkrsoft.framework.config.v1.RuntimeMode;
import org.apache.commons.lang.StringUtils;

/**
 * Created by rnkrsoft.com on 2018/4/24.
 */
public class ConfigCli {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 80;
        String groupId = null;
        String artifactId = null;
        String version = null;
        String env = null;
        int fetchDelaySeconds = 0;
        int fetchIntervalSeconds = 60;
        RuntimeMode configMode = RuntimeMode.AUTO;
        String remoteCacheHome = ".";
        for (int i = 0; i < args.length; i++) {
            String name = args[i];
            if (name.startsWith("-host=")) {
                String value = name.substring("-host=".length());
                host = value;
            }
            if (name.startsWith("-port=")) {
                String value = name.substring("-port=".length());
                port = Integer.valueOf(value);
            }
            if (name.startsWith("-groupId=")) {
                String value = name.substring("-groupId=".length());
                groupId = value;
            }
            if (name.startsWith("-artifactId=")) {
                String value = name.substring("-artifactId=".length());
                artifactId = value;
            }
            if (name.startsWith("-version=")) {
                String value = name.substring("-version=".length());
                version = value;
            }
            if (name.startsWith("-env=")) {
                String value = name.substring("-env=".length());
                env = value;
            }
            if (name.startsWith("-fetchIntervalSeconds=")) {
                String value = name.substring("-fetchIntervalSeconds=".length());
                fetchIntervalSeconds = Integer.valueOf(value);
            }
            if (name.startsWith("-configMode=")) {
                String value = name.substring("-configMode=".length());
                configMode = RuntimeMode.valueOf(value);
            }
            if (name.startsWith("-remoteCacheHome=")) {
                String value = name.substring("-remoteCacheHome=".length());
                remoteCacheHome = value;
            }
        }
        if(StringUtils.isBlank(groupId)){
            throw new Error("未配置groupId");
        }
        if(StringUtils.isBlank(artifactId)){
            throw new Error("未配置artifactId");
        }
        if(StringUtils.isBlank(version)){
            throw new Error("未配置version");
        }
        if(StringUtils.isBlank(env)){
            throw new Error("未配置env");
        }
        ConfigClientSetting setting = ConfigClientSetting.builder()
                .host(host)
                .port(port)
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .env(env)
                .machine(MachineUtils.getHostName())
                .runtimeMode(configMode)
                .workHome(remoteCacheHome)
                .build();
        ConfigClient configClient = ConfigClient.getInstance();
        configClient.init(setting);
        while (true) {
            configClient.fetch();
            Thread.sleep(fetchIntervalSeconds * 1000);
        }
    }
}
