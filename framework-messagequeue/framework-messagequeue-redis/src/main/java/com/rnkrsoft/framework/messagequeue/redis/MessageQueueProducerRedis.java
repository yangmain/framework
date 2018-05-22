package com.rnkrsoft.framework.messagequeue.redis;

import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
public class MessageQueueProducerRedis extends AbstractMessageQueueProducer {
    @Override
    public int produce(Message message) {
        return SUCCESS;
    }
}
