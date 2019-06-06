package com.rnkrsoft.framework.config.client;

import com.rnkrsoft.framework.config.v1.ConnectorType;
import com.rnkrsoft.framework.config.v1.RuntimeMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
@Getter
@ToString
public class ConfigClientSetting {
    /**
     * 打印日志
     */
    boolean printLog;
    /**
     * 工作模式，使用的通信方式，默认HTTP
     */
    ConnectorType connectorType;
    /**
     * 运行时模式，默认使用自动模式
     */
    RuntimeMode runtimeMode;
    /**
     * 中心服务器IP地址
     */
    String host;
    /**
     * 服务器端口号
     */
    int port;
    /**
     * 组织编号
     */
    String groupId;
    /**
     * 组件编号
     */
    String artifactId;
    /**
     * 版本
     */
    String version;
    /**
     * 描述
     */
    String desc;
    /**
     * 环境
     */
    String env;
    /**
     * 机器编号
     */
    String machine;
    /**
     * 安全密钥
     */
    String securityKey;
    /**
     * 工作路径
     */
    String workHome;
    /**
     * 文件编码
     */
    String fileEncoding;
    /**
     * 拉取参数间隔秒数
     */
    long fetchIntervalSeconds;

    private ConfigClientSetting() {
    }

    public static class ConfigClientSettingBuilder{
        /**
         * 打印日志
         */
        boolean printLog = true;
        /**
         * 工作模式，使用的通信方式，默认HTTP
         */
        ConnectorType connectorType = ConnectorType.HTTP;
        /**
         * 运行时模式，默认使用自动模式
         */
        RuntimeMode runtimeMode = RuntimeMode.AUTO;
        /**
         * 中心服务器IP地址
         */
        String host = "localhost";
        /**
         * 服务器端口号
         */
        int port = 80;
        /**
         * 组织编号
         */
        String groupId;
        /**
         * 组件编号
         */
        String artifactId;
        /**
         * 版本
         */
        String version;
        /**
         * 描述
         */
        String desc;
        /**
         * 环境
         */
        String env;
        /**
         * 机器编号
         */
        String machine;
        /**
         * 安全密钥
         */
        String securityKey;
        /**
         * 工作路径
         */
        String workHome = System.getProperty("user.dir") + "/work";
        /**
         * 文件编码
         */
        String fileEncoding = "UTF-8";
        /**
         * 拉取参数间隔秒数
         */
        long fetchIntervalSeconds = 60;

        private ConfigClientSettingBuilder() {
        }

        public ConfigClientSettingBuilder printLog(boolean printLog) {
            this.printLog = printLog;
            return this;
        }

        public ConfigClientSettingBuilder connectorType(ConnectorType connectorType) {
            if (connectorType == null){
                return this;
            }
            this.connectorType = connectorType;
            return this;
        }

        public ConfigClientSettingBuilder runtimeMode(RuntimeMode runtimeMode) {
            this.runtimeMode = runtimeMode;
            return this;
        }

        public ConfigClientSettingBuilder host(String host) {
            this.host = host;
            return this;
        }

        public ConfigClientSettingBuilder port(int port) {
            this.port = port;
            return this;
        }

        public ConfigClientSettingBuilder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public ConfigClientSettingBuilder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public ConfigClientSettingBuilder version(String version) {
            this.version = version;
            return this;
        }

        public ConfigClientSettingBuilder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public ConfigClientSettingBuilder env(String env) {
            this.env = env;
            return this;
        }

        public ConfigClientSettingBuilder machine(String machine) {
            this.machine = machine;
            return this;
        }

        public ConfigClientSettingBuilder securityKey(String securityKey) {
            this.securityKey = securityKey;
            return this;
        }

        public ConfigClientSettingBuilder workHome(String workHome) {
            if (workHome == null){
                return this;
            }
            this.workHome = workHome;
            return this;
        }

        public ConfigClientSettingBuilder fileEncoding(String fileEncoding) {
            if (fileEncoding == null){
                return this;
            }
            this.fileEncoding = fileEncoding;
            return this;
        }

        public ConfigClientSettingBuilder fetchIntervalSeconds(long fetchIntervalSeconds) {
            this.fetchIntervalSeconds = fetchIntervalSeconds;
            return this;
        }

        public ConfigClientSetting build(){
            ConfigClientSetting setting = new ConfigClientSetting();
            setting.printLog = printLog;
            setting.connectorType = connectorType;
            setting.runtimeMode = runtimeMode;
            setting.host = host;
            setting.port = port;
            setting.groupId = groupId;
            setting.artifactId = artifactId;
            setting.version = version;
            setting.desc = desc;
            setting.env = env;
            setting.machine = machine;
            setting.securityKey = securityKey;
            setting.workHome = workHome;
            setting.fileEncoding = fileEncoding;
            setting.fetchIntervalSeconds = fetchIntervalSeconds;
            return setting;
        }
    }
    public static ConfigClientSettingBuilder builder(){
        return new ConfigClientSettingBuilder();
    }
}
