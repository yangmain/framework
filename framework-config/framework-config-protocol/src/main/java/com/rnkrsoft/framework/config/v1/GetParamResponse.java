package com.rnkrsoft.framework.config.v1;

import lombok.Data;

import javax.web.doc.AbstractResponse;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetParamResponse extends AbstractResponse {
    /**
     * 请求唯一标识
     */
    String id;
    /**
     * 配置参数列表z
     */
    final List<ParamObject> params = new ArrayList();
}
