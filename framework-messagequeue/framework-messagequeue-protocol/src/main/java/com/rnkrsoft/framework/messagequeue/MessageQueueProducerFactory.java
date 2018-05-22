package com.rnkrsoft.framework.messagequeue;

import com.devops4j.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueProducer;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public class MessageQueueProducerFactory {

    public MessageQueueProducer getInstance() {
        return getInstance(null);
    }

    public MessageQueueProducer getInstance(String implementClassName) {
        MessageQueueProducer service = null;
        ServiceLoader<MessageQueueProducer> serviceLoader = ServiceLoader.load(MessageQueueProducer.class);
        Iterator<MessageQueueProducer> serviceIterator = serviceLoader.iterator();
        while (service == null && serviceIterator.hasNext()) {
            MessageQueueProducer service0 = serviceIterator.next();
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
                    .activity("scan MessageQueueProducer")
                    .message("not found implement class")
                    .runtimeException();
        }
        return service;
    }
}
