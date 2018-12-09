package com.rnkrsoft.framework.messagequeue.aliyunmq;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Slf4j
public class MessageQueueProducerAliyunMQ extends AbstractMessageQueueProducer {
    @Setter
    String uri;
    @Setter
    String producerId;
    @Setter
    String accessKey;
    @Setter
    String secretKey;
    @Setter
    String topic;
    Producer producer;

    @Override
    public void setUsername(String username) {
        throw new UnsupportedOperationException("不支持username");
    }

    @Override
    public void setPassword(String password) {
        throw new UnsupportedOperationException("不支持password");
    }

    @Override
    public void init() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, producerId);// 您在控制台创建的 Producer ID
        properties.put(PropertyKeyConst.AccessKey, accessKey);// AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.ONSAddr, uri);//此处以公共云生产环境为例
        this.producer = ONSFactory.createProducer(properties);
        this.producer.start();
    }

    @Override
    public void destroy() {
        this.producer.shutdown();
    }

    @Override
    public int produce(Message message) {
        try {
            com.aliyun.openservices.ons.api.Message msg = new com.aliyun.openservices.ons.api.Message(topic, message.getRoutingKey(), message.asJson().getBytes("UTF-8"));
            msg.setKey(UUID.randomUUID().toString());
            msg.setMsgID(UUID.randomUUID().toString());
            if (log.isDebugEnabled()){
                log.debug("send message:'{}'", msg);
            }
            this.producer.send(msg);
            return SUCCESS;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return FAILURE;
        }

    }
}
