package com.rnkrsoft.framework.messagequeue.producer;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueProducer;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public abstract class AbstractMessageQueueProducer implements MessageQueueProducer {
    @Override
    public int produce(Object bean) {
        com.rnkrsoft.framework.messagequeue.annotation.Message messageAnn = bean.getClass().getAnnotation(com.rnkrsoft.framework.messagequeue.annotation.Message.class);
        String routingKey = messageAnn.routingKey();
        Message message = new Message(bean);
        message.setRoutingKey(routingKey);
        return produce(message);
    }
}
