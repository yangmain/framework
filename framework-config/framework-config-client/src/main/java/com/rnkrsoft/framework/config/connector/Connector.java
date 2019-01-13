package com.rnkrsoft.framework.config.connector;

import com.rnkrsoft.framework.config.v1.FetchRequest;
import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.PushRequest;
import com.rnkrsoft.framework.config.v1.PushResponse;

/**
 * 连接器接口
 */
public interface Connector {
    /**
     * 设置打印日志
     * @param printLog 日志
     */
    void setPrintLog(boolean printLog);
    /**
     * 启动连接器
     */
    void start();

    /**
     * 停止连接器
     */
    void stop();

    /**
     * 主动拉取配置
     *
     * @return 拉取配置应答
     * @throws Exception 异常
     */
    FetchResponse fetch(FetchRequest request);

    /**
     * 向服务器推送配置
     * @param request
     * @throws Exception
     */
    PushResponse push(PushRequest request);
}