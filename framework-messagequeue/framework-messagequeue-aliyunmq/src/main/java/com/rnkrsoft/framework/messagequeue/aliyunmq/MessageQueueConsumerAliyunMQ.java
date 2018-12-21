package com.rnkrsoft.framework.messagequeue.aliyunmq;

import com.aliyun.openservices.ons.api.*;
import com.rnkrsoft.framework.messagequeue.consumer.AbstractMessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.ConsumerType;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 * 基于阿里云MQ的消息队列消费者实现
 */
@Slf4j
public class MessageQueueConsumerAliyunMQ extends AbstractMessageQueueConsumer implements InitializingBean{
    @Setter
    String consumerId;
    @Setter
    String accessKey;
    @Setter
    String secretKey;
    @Setter
    String topic;
    @Setter
    String uri;
    /**
     * 消费者线程池数
     */
    @Setter
    int consumeThreadNum;

    Consumer consumer;
    /**
     * 通过Spring配置进行监听器注册
     */
    @Setter
    List<MessageQueueListener> messageQueueListeners;

    MessageQueueListener lookupListener(String routingKey) {
        for (MessageQueueListener listener : listeners) {
            List<MessageQueueSelector> selectors = listener.getSelectors();
            for (MessageQueueSelector selector : selectors) {
                if (selector.accept(routingKey)) {
                    return listener;
                }
            }
        }
        return null;
    }

    String getTag() {
        StringBuilder tag = new StringBuilder();
        for (MessageQueueListener listener : listeners) {
            List<MessageQueueSelector> selectors = listener.getSelectors();
            for (MessageQueueSelector selector : selectors) {
                String routingKey = selector.getRoutingKey();
                int lastDotIdx = routingKey.lastIndexOf(".");
                tag.append("||").append(routingKey.substring(lastDotIdx + 1));
            }
        }
        if (tag.length() > 0) {
            tag.delete(0, 2);
        }
        return tag.toString();
    }

    @Override
    protected void init() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, accessKey);// AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.ONSAddr, uri);//此处以公共云生产环境为例
        properties.put(PropertyKeyConst.ConsumerId, consumerId);// 您在控制台创建的 Consumer ID
        properties.put(PropertyKeyConst.ConsumeThreadNums, consumeThreadNum);
        if (log.isDebugEnabled()) {
            log.debug("consumer '{}' init...", consumerId);
        }
        if (uri == null || uri.isEmpty()) {
            throw ErrorContextFactory.instance()
                    .message("AliyunMQ consumer is not config uri!")
                    .solution("please set http://onsaddr-internal.aliyun.com:8080/rocketmq/nsaddr4client-internal")
                    .runtimeException();
        }
        if (messageQueueListeners == null || messageQueueListeners.isEmpty()) {
            throw ErrorContextFactory.instance()
                    .message("AliyunMQ consumer is not config messageQueueListeners!")
                    .solution("please set message Queue Listeners")
                    .runtimeException();
        }
        try {
            if (messageQueueListeners != null){
                for (MessageQueueListener listener : messageQueueListeners){
                    registerListener(listener);
                }
            }
            this.consumer = ONSFactory.createConsumer(properties);
            this.consumer.subscribe(topic, getTag(), new MessageListener() {
                @Override
                public Action consume(Message message, ConsumeContext context) {
                    String sessionId = message.getKey();
                    int age = message.getReconsumeTimes();
                    MDC.put("sessionId", sessionId);
                    String json = null;
                    try {
                        json = new String(message.getBody(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        log.error("receive message happen error!", e);
                    }
                    String routingKey = message.getTag();

                    if (log.isDebugEnabled()) {
                        log.debug("receive message: '{}' routingKey:'{}' age:'{}'", json, routingKey, age);
                    }
                    //topic + tag构成路由关键字
                    MessageQueueListener listener = lookupListener(message.getTag());
                    if (listener == null) {
                        return Action.ReconsumeLater;
                    }
                    com.rnkrsoft.framework.messagequeue.protocol.Message msg = com.rnkrsoft.framework.messagequeue.protocol.Message.message(json);
                    msg.setAge(age);
                    msg.setCreateDate(message.getBornTimestamp());
                    if (log.isDebugEnabled()) {
                        log.debug("call listener '{}'", msg);
                    }
                    listener.execute(msg);
                    return Action.CommitMessage;
                }
            });
            this.consumer.start();
        }catch (Exception e){
            log.error("aliyunMQ consumer init happens error!", e);
            throw ErrorContextFactory.instance()
                    .message("aliyunMQ consumer init happens error!")
                    .solution("检查Aliyun MQ是否已启动")
                    .runtimeException();
        }
    }

    @Override
    public int shutdown() {
        return 0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
