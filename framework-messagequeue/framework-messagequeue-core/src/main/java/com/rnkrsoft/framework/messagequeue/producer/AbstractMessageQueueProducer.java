package com.rnkrsoft.framework.messagequeue.producer;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueProducer;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public abstract class AbstractMessageQueueProducer implements MessageQueueProducer{
    @Override
    public int produce(Object bean) {
        return produce(new Message(bean));
    }
}
