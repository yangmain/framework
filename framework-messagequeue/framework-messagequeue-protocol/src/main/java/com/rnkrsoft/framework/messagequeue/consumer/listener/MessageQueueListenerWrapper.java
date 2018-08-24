package com.rnkrsoft.framework.messagequeue.consumer.listener;

import com.rnkrsoft.framework.messagequeue.protocol.AbstractMessageQueueListener;
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
public class MessageQueueListenerWrapper<T> extends AbstractMessageQueueListener<T> implements MessageQueueListener<T>{
    MessageQueueListener<T> proxy;
    boolean whenErrorRequeue;
    int maxTryProcessAge;
    boolean ack;

    public MessageQueueListenerWrapper(MessageQueueListener<T> proxy, boolean whenErrorRequeue, int maxTryProcessAge, boolean ack, List<MessageQueueSelector> selectors) {
        super(selectors);
        this.proxy = proxy;
        getSelectors().addAll(proxy.getSelectors());
        this.whenErrorRequeue = whenErrorRequeue;
        this.maxTryProcessAge = maxTryProcessAge;
        this.ack = ack;
    }

    @Override
    public void execute(Message<T> message) {
        this.proxy.execute(message);
    }
}
