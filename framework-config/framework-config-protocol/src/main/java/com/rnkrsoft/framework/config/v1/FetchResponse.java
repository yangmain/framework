package com.rnkrsoft.framework.config.v1;

import com.rnkrsoft.framework.config.ProtocolConstants;
import lombok.*;

import javax.web.doc.AbstractResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/7.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchResponse extends AbstractResponse {
    /**
     * 请求唯一标识
     */
    String id;
    /**
     * 通信协议版本
     */
    final int formatVersion = ProtocolConstants.VERSION_1;
    /**
     * 配置文件列表
     */
    final List<FileObject> files = new ArrayList();
    /**
     * 配置参数列表
     */
    final List<ParamObject> params = new ArrayList();
    /**
     * 变动时间戳，如果客户端保存的上次变更与当次大于或者等于，则不进行参数处理操作
     */
    long updateTimestamp;
}
