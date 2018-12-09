package com.rnkrsoft.framework.messagequeue.producer;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueProducer;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public abstract class AbstractMessageQueueProducer implements MessageQueueProducer {
    @Override
    public int produce(Object bean) {
        com.rnkrsoft.framework.messagequeue.annotation.Message messageAnn = bean.getClass().getAnnotation(com.rnkrsoft.framework.messagequeue.annotation.Message.class);
        if (messageAnn == null){
            throw ErrorContextFactory.instance()
                    .message("未定义@Message")
                    .solution("在类{}添加@com.rnkrsoft.framework.messagequeue.annotation.Message", bean.getClass())
                    .runtimeException();
        }
        String routingKey = messageAnn.routingKey();
        Message message = new Message(bean);
        message.setRoutingKey(routingKey);
        return produce(message);
    }
}
