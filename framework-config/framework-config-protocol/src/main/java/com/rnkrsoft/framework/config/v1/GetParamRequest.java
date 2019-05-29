package com.rnkrsoft.framework.config.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetParamRequest implements Serializable {
    /**
     * 请求唯一标识
     */
    String id;
    /**
     * 参数名
     */
    final List<String> keys = new ArrayList();
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
}
