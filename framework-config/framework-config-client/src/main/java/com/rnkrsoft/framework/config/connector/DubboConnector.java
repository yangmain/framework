package com.rnkrsoft.framework.config.connector;

import com.rnkrsoft.framework.config.v1.FetchRequest;
import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.PushRequest;
import com.rnkrsoft.framework.config.v1.PushResponse;
import lombok.Setter;

/**
 * Created by rnkrsoft.com on 2018/5/9.
 */
@Setter
public class DubboConnector implements Connector{
    /**
     * 打印信息
     */
    boolean printLog = false;
    /**
     * 注册中心类型
     */
    String registerType;
    /**
     * 注册中心地址
     */
    String registerHost;
    /**
     * 注册中心端口
     */
    int registerPort;


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public FetchResponse fetch(FetchRequest request) {
        //TODO 调用远程服务接口
        return null;
    }

    @Override
    public PushResponse push(PushRequest request) {
        return null;
    }
}
