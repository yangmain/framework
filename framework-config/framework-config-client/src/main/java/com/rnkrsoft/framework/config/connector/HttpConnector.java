package com.rnkrsoft.framework.config.connector;

import com.rnkrsoft.framework.config.v1.*;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rnkrsoft.framework.config.utils.Http;
import com.rnkrsoft.message.MessageFormatter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

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

    public HttpConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void start() {
    }

    public void stop() {

    }

    public FetchResponse fetch(FetchRequest request) {
        String json = gson.toJson(request);
        String url = MessageFormatter.format("http://{}:{}/api/fetch", host, port);
        if (log.isDebugEnabled()) {
            log.debug("fetch data '{}':'{}'", json);
        }
        Http http = Http.post(url)
                .acceptCharset("UTF-8")
                .connectTimeout(30000)
                .readTimeout(30000)
                .useCaches(false)//不允许缓存
                .contentType("application/json;text/plain", "UTF-8");
        try {
            http.send(json);
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .message("fetch from config center happens error!")
                    .cause(e.getCause())
                    .solution("please check network")
                    .runtimeException();
        }
        //如果返回200
        if (http.ok()) {
            String rsp = http.body("UTF-8");
            FetchResponse response = gson.fromJson(rsp, FetchResponse.class);
            MDC.put("sessionId", response.getId());
            if (log.isDebugEnabled()) {
                log.debug("fetch success, data : {}", gson.toJson(response));
            }
            return response;
            //如果返回404
        } else if (http.notFound()) {
            throw ErrorContextFactory.instance()
                    .message("fetch from config center happens error! cause:'{}'", http.code())
                    .solution("please check network")
                    .runtimeException();
        } else {//处理不了的未知错误
            throw ErrorContextFactory.instance()
                    .message("fetch from config center happens error! cause:'{}'", http.code())
                    .solution("please check network")
                    .runtimeException();
        }
    }

    public GetParamResponse getParam(GetParamRequest request) {
        String json = gson.toJson(request);
        String url = MessageFormatter.format("http://{}:{}/api/getParam", host, port);
        if (log.isDebugEnabled()) {
            log.debug("get data '{}':'{}'", json);
        }
        Http http = Http.post(url)
                .acceptCharset("UTF-8")
                .connectTimeout(30000)
                .readTimeout(30000)
                .useCaches(false)//不允许缓存
                .contentType("application/json;text/plain", "UTF-8");
        try {
            http.send(json);
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .message("getParam from config center happens error!")
                    .cause(e.getCause())
                    .solution("please check network")
                    .runtimeException();
        }
        //如果返回200
        if (http.ok()) {
            String rsp = http.body("UTF-8");
            GetParamResponse response = gson.fromJson(rsp, GetParamResponse.class);
            MDC.put("sessionId", response.getId());
            if (log.isDebugEnabled()) {
                log.debug("getParam success, data : {}", gson.toJson(response));
            }
            return response;
            //如果返回404
        } else if (http.notFound()) {
            throw ErrorContextFactory.instance()
                    .message("getParam from config center happens error! cause:'{}'", http.code())
                    .solution("please check network")
                    .runtimeException();
        } else {//处理不了的未知错误
            throw ErrorContextFactory.instance()
                    .message("getParam from config center happens error! cause:'{}'", http.code())
                    .solution("please check network")
                    .runtimeException();
        }
    }

    public PushResponse push(PushRequest request) {
        String json = gson.toJson(request);
        String url = MessageFormatter.format("http://{}:{}/api/push", host, port);
        if (log.isDebugEnabled()) {
            log.debug("push data '{}':'{}'", url, json);
        }
        Http http = Http.post(url)
                .acceptCharset("UTF-8")
                .connectTimeout(30000)
                .readTimeout(30000)
                .useCaches(false)//不允许缓存
                .contentType("application/json;text/plain", "UTF-8");
        try {
            http.send(json);
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .message("push to config center happens error!")
                    .cause(e.getCause())
                    .solution("please check network")
                    .runtimeException();
        }
        //如果返回200
        if (http.ok()) {
            String rsp = http.body("UTF-8");
            PushResponse response = gson.fromJson(rsp, PushResponse.class);
            if (printLog) {
                log.debug("push response: {}", rsp);
            }
            return response;
            //如果返回404
        } else if (http.notFound()) {
            throw ErrorContextFactory.instance()
                    .message("push to config center happens error! cause:'{}'", http.code())
                    .solution("please check network")
                    .runtimeException();
        } else {//处理不了的未知错误
            throw ErrorContextFactory.instance()
                    .message("push to config center happens error! cause:'{}'", http.code())
                    .solution("please check network")
                    .runtimeException();
        }
    }
}
