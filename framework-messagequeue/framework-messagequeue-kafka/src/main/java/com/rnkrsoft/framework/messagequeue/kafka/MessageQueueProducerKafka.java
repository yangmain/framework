package com.rnkrsoft.framework.messagequeue.kafka;

import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
public class MessageQueueProducerKafka extends AbstractMessageQueueProducer {
    @Override
    public int produce(Message message) {
        return SUCCESS;
    }
}
