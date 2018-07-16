package com.rnkrsoft.framework.messagequeue.aliyunmq;

import com.aliyun.openservices.ons.api.*;
import com.rnkrsoft.framework.messagequeue.consumer.AbstractMessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.ConsumerType;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Slf4j
public class MessageQueueConsumerAliyunMQ extends AbstractMessageQueueConsumer {
    @Setter
    String consumerId;
    @Setter
    String accessKey;
    @Setter
    String secretKey;
    @Setter
    String topic;
    @Setter
    String url;
    @Setter
    int consumeThreadNum;
    Consumer consumer;

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
        if (tag.length() > 0){
            tag.delete(0, 2);
        }
        return tag.toString();
    }

    @Override
    public int startup(ConsumerType type) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, accessKey);// AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.ONSAddr, url);//此处以公共云生产环境为例
        properties.put(PropertyKeyConst.ConsumerId, consumerId);// 您在控制台创建的 Consumer ID
        properties.put(PropertyKeyConst.ConsumeThreadNums, consumeThreadNum);
        if (log.isDebugEnabled()){
            log.debug("consumer '{}' init...", consumerId);
        }
        this.consumer = ONSFactory.createConsumer(properties);
        this.consumer.subscribe(topic, getTag(), new MessageListener() {
            @Override
            public Action consume(Message message, ConsumeContext context) {
                if (log.isDebugEnabled()) {
                    log.debug("receive message: '{}' routingKey:'{}'", message, message.getTopic() + "." + message.getTag());
                }
                //topic + tag构成路由关键字
                MessageQueueListener listener = lookupListener(message.getTopic() + "." + message.getTag());
                if(listener == null){
                    return Action.ReconsumeLater;
                }
                try {
                    com.rnkrsoft.framework.messagequeue.protocol.Message msg = com.rnkrsoft.framework.messagequeue.protocol.Message.message(new String(message.getBody(), "UTF-8"));
                    if (log.isDebugEnabled()){
                        log.debug("call listener '{}'", msg);
                    }
                    listener.execute(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Action.CommitMessage;
            }
        });
        this.consumer.start();
        return SUCCESS;
    }

    @Override
    public int startup() {
        return startup(ConsumerType.HEAD);
    }

    @Override
    public int shutdown() {
        return 0;
    }
}
