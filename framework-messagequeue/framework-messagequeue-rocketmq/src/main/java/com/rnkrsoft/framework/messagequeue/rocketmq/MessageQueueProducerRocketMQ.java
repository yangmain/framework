package com.rnkrsoft.framework.messagequeue.rocketmq;

import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import lombok.Data;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Data
public class MessageQueueProducerRocketMQ extends AbstractMessageQueueProducer {
    String username;
    String password;
    String url;

    @Override
    public void init() {

    }

    @Override
    public int produce(Message message) {
        return SUCCESS;
    }
}
