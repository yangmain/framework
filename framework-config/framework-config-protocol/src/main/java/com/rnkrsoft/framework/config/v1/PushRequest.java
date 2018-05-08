package com.rnkrsoft.framework.config.v1;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
@Data
@ToString
@Builder
public class PushRequest {
    /**
     * 只读方式,并不会覆盖，用于进行参数比较
     */
    boolean readonly;
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
     * 描述信息
     */
    String desc;
    /**
     * 环境
     */
    String env;
    /**
     * 机器标识
     */
    String machine;
    /**
     * 网卡地址号
     */
    String mac;
    /**
     * 参数列表
     */
    final List<ParamObject> params = new ArrayList();
    /**
     * 文件列表
     */
    final List<FileObject> files = new ArrayList();
}
