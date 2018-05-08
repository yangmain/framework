package com.rnkrsoft.framework.config.connector;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.devops4j.message.MessageFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.config.Debug;
import com.rnkrsoft.framework.config.utils.Http;
import com.rnkrsoft.framework.config.v1.FetchRequest;
import com.rnkrsoft.framework.config.v1.FetchResponse;
import com.rnkrsoft.framework.config.v1.PushRequest;
import com.rnkrsoft.framework.config.v1.PushResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;

/**
 * Created by rnkrsoft.com on 2018/5/8.
 */
@Data
@Slf4j
public class HttpConnector implements Connector {
    /**
     * 打印信息
     */
    boolean printLog = false;
    /**
     * 主机地址
     */
    String host;
    /**
     * 端口
     */
    int port;

    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().setDateFormat("yyyyMMddHHmmssSSS").create();

    String fetchConfigCenterUrl = null;
    String pushConfigCenterUrl = null;

    public HttpConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }


    @Override
    public void start() {
        {
            StringBuilder urlBuffer = new StringBuilder();
            urlBuffer.append("https://").append(host).append(":").append(port).append("/api/fetch");
            this.fetchConfigCenterUrl = urlBuffer.toString();
        }
        {
            StringBuilder urlBuffer = new StringBuilder();
            urlBuffer.append("https://").append(host).append(":").append(port).append("/api/push");
            this.pushConfigCenterUrl = urlBuffer.toString();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public FetchResponse fetch(FetchRequest request) {
        String json = gson.toJson(request);
        if (log.isDebugEnabled()) {
            log.info("fetch data:'{}'", json);
        }
        if (Debug.DEBUG) {
            FetchResponse response = FetchResponse.builder().build();
            return response;
        }
        Http http = Http.post(fetchConfigCenterUrl)
                .acceptCharset("UTF-8")
                .connectTimeout(5000)
                .readTimeout(5000)
                .useCaches(false)//不允许缓存
                .contentType("application/json;text/plain", "UTF-8");
        try {
            http.send(json);
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .message("fetch from config center '{}' happens error!", fetchConfigCenterUrl)
                    .cause(e.getCause())
                    .solution("please check network")
                    .runtimeException();
        }
        //如果返回200
        if (http.ok()) {
            String rsp = http.body("UTF-8");
            FetchResponse response = gson.fromJson(rsp, FetchResponse.class);
            MDC.put("sessionId", response.getId());
            response.setId("");
            if (printLog) {
                log.info("fetch success, data : {}", gson.toJson(response));
            }
            return response;
            //如果返回404
        } else if (http.notFound()) {
            throw ErrorContextFactory.instance()
                    .message("fetch from config center '{}' happens error! cause:'{}'", fetchConfigCenterUrl, http.code())
                    .solution("please check network")
                    .runtimeException();
        } else {//处理不了的未知错误
            throw ErrorContextFactory.instance()
                    .message("fetch from config center '{}' happens error! cause:'{}'", fetchConfigCenterUrl, http.code())
                    .solution("please check network")
                    .runtimeException();
        }
    }

    @Override
    public PushResponse push(PushRequest request) {
        String json = gson.toJson(request);
        if (log.isDebugEnabled()) {
            log.info("push data:'{}'", json);
        }
        if (Debug.DEBUG) {
            PushResponse response = PushResponse.builder().build();
            return response;
        }
        Http http = Http.post(pushConfigCenterUrl)
                .acceptCharset("UTF-8")
                .connectTimeout(5000)
                .readTimeout(5000)
                .useCaches(false)//不允许缓存
                .contentType("application/json;text/plain", "UTF-8");
        try {
            http.send(json);
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .message("push to config center '{}' happens error!", pushConfigCenterUrl)
                    .cause(e.getCause())
                    .solution("please check network")
                    .runtimeException();
        }
        //如果返回200
        if (http.ok()) {
            String rsp = http.body("UTF-8");
            PushResponse response = gson.fromJson(rsp, PushResponse.class);
            if (printLog) {
                log.info("push response: {}", rsp);
            }
            return response;
            //如果返回404
        } else if (http.notFound()) {
            throw ErrorContextFactory.instance()
                    .message("push to config center '{}' happens error! cause:'{}'", pushConfigCenterUrl, http.code())
                    .solution("please check network")
                    .runtimeException();
        } else {//处理不了的未知错误
            throw ErrorContextFactory.instance()
                    .message("push to config center '{}' happens error! cause:'{}'", pushConfigCenterUrl, http.code())
                    .solution("please check network")
                    .runtimeException();
        }
    }
}
