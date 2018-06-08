package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
public class MessageQueueProducerRabbitMQ extends AbstractMessageQueueProducer {
    @Override
    public void setUrl(String url) {

    }

    @Override
    public void setUsername(String username) {

    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public void init() {

    }

    @Override
    public int produce(Message message) {
        return SUCCESS;
    }
}
