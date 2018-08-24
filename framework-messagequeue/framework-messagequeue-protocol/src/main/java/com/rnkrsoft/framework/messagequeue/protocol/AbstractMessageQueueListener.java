package com.rnkrsoft.framework.messagequeue.protocol;

import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/7/13.
 */
public abstract class AbstractMessageQueueListener<T> implements MessageQueueListener<T>{
    @Getter
    protected final List<MessageQueueSelector> selectors = new ArrayList();

    public AbstractMessageQueueListener(List<MessageQueueSelector> selectors) {
        this.selectors.addAll(selectors);
    }
}
