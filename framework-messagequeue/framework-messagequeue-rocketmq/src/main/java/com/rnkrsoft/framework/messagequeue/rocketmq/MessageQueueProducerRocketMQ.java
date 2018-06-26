package com.rnkrsoft.framework.messagequeue.rocketmq;

import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import lombok.Data;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Data
public class MessageQueueProducerRocketMQ extends AbstractMessageQueueProducer {
    String username;
    String password;
    String url;
    DefaultMQProducer producer;

    @Override
    public void init() {
        producer = new DefaultMQProducer("Producer");
        producer.setNamesrvAddr("");
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int produce(Message message) {
        try {
            org.apache.rocketmq.common.message.Message msg = new  org.apache.rocketmq.common.message.Message("PushTopic", "push", "1", "Just for push1.".getBytes());
            SendResult result = producer.send(msg);
            System.out.println("id:" + result.getMsgId() + " result:" + result.getSendStatus());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return SUCCESS;
    }
}
