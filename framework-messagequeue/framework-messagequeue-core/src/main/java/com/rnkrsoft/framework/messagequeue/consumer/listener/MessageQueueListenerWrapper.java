package com.rnkrsoft.framework.messagequeue.consumer.listener;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 * 通过对消息队列监听器进行增强，将注解上的信息转换为成员属性
 */
@Getter
public class MessageQueueListenerWrapper<T> implements MessageQueueListener<T>{
    MessageQueueListener<T> proxy;
    final List<MessageQueueSelector> selectors = new ArrayList();
    boolean whenErrorRequeue;
    int maxTryProcessAge;
    boolean ack;

    public MessageQueueListenerWrapper(MessageQueueListener<T> proxy, boolean whenErrorRequeue, int maxTryProcessAge, boolean ack, List<MessageQueueSelector> selectors) {
        this.proxy = proxy;
        this.whenErrorRequeue = whenErrorRequeue;
        this.maxTryProcessAge = maxTryProcessAge;
        this.ack = ack;
        this.selectors.addAll(selectors);
    }

    @Override
    public void execute(Message<T> message) {
        this.proxy.execute(message);
    }
}