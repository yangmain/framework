package com.rnkrsoft.framework.config.v1;

import com.rnkrsoft.framework.config.ProtocolConstants;
import lombok.*;

import javax.web.doc.AbstractResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by woate on 2018/5/7.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchResponse extends AbstractResponse{
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
     * 配置文件列表
     */
    final List<FileObject> files = new ArrayList();
    /**
     * 配置参数列表
     */
    final List<ParamObject> params = new ArrayList();
}
