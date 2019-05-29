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
@Builder
@Getter
@ToString
public class ConfigClientSetting {
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
    String workHome = System.getProperty("user.dir") + "/config";
    /**
     * 文件编码
     */
    String fileEncoding = "UTF-8";
    /**
     * 拉取参数间隔秒数
     */
    long fetchIntervalSeconds = 60;
}
