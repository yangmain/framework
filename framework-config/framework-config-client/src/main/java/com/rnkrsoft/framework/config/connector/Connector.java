package com.rnkrsoft.framework.config.connector;

import com.rnkrsoft.framework.config.v1.*;

/**
 * 连接器接口
 * Created by rnkrsoft.com on 2018/5/8.
 */
public interface Connector {
    /**
     * 设置打印日志
     *
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
     * @param request 请求
     * @return 应答
     */
    FetchResponse fetch(FetchRequest request);

    /**
     * 获取参数
     *
     * @param request 请求
     * @return 应答
     */
    GetParamResponse getParam(GetParamRequest request);

    /**
     * 向服务器推送配置
     *
     * @param request 请求
     * @return 应答
     */
    PushResponse push(PushRequest request);
}