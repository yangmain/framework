package com.rnkrsoft.framework.messagequeue.protocol;


import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;

import java.util.List;

/**
 * Created by rnkrsoft on 2018/5/21.
 * 消息队列监听器
 */
public interface MessageQueueListener<T> {
    List<MessageQueueSelector> getSelectors();
    /**
     * 执行监听器处理消息对象
     * @param message
     */
    void execute(Message<T> message);
}
