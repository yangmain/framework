package com.rnkrsoft.framework.config.v1;

import com.rnkrsoft.framework.config.ProtocolConstants;
import lombok.*;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 * 拉取参数请求对象
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchRequest {
    /**
     * 请求唯一标识
     */
    String id;
    /**
     * 通信协议版本
     */
    final int formatVersion = ProtocolConstants.VERSION_1;
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
     * 环境
     */
    String env;
    /**
     * 机器标识
     */
    String machine;
    /**
     * MAC网卡地址，用于获取证书
     */
    String mac;
}
