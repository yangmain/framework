package com.rnkrsoft.framework.orm.messagequeue.proxy;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueProducer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/7/12.
 */
public class MessageQueueProxy implements InvocationHandler {

    MessageQueueProducer messageQueueProducer;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Message message = new Message(args[0]);
        int sendResult = messageQueueProducer.produce(message);
        return null;
    }
}
