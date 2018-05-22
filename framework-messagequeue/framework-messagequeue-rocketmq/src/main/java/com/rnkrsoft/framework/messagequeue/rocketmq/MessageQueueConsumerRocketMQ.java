package com.rnkrsoft.framework.messagequeue.rocketmq;

import com.rnkrsoft.framework.messagequeue.consumer.AbstractMessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.protocol.ConsumerType;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
public class MessageQueueConsumerRocketMQ extends AbstractMessageQueueConsumer {
    @Override
    public int startup(ConsumerType type) {
        return SUCCESS;
    }

    @Override
    public int startup() {
        return startup(ConsumerType.HEAD);
    }
}
