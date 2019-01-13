package com.rnkrsoft.framework.messagequeue;

import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueConsumer;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public class MessageQueueConsumerFactory {

    public MessageQueueConsumer getInstance() {
        return getInstance(null);
    }

    public MessageQueueConsumer getInstance(String implementClassName) {
        MessageQueueConsumer service = null;
        ServiceLoader<MessageQueueConsumer> serviceLoader = ServiceLoader.load(MessageQueueConsumer.class);
        Iterator<MessageQueueConsumer> serviceIterator = serviceLoader.iterator();
        while (service == null && serviceIterator.hasNext()) {
            MessageQueueConsumer service0 = serviceIterator.next();
            //如果没有指定实现类，发现则赋值
            if (implementClassName == null) {
                service = service0;
            } else {//如果指实现类，只有匹配实现类才赋值
                if (service0.getClass().getName().equals(implementClassName)) {
                    service = service0;
                }
            }
        }
        if (service == null) {
            throw ErrorContextFactory.instance()
                    .activity("scan MessageQueueConsumer")
                    .message("not found implement class")
                    .runtimeException();
        }
        return service;
    }
}
