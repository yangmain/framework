package com.rnkrsoft.framework.messagequeue.consumer;

import com.rnkrsoft.framework.messagequeue.annotation.ListenerDefinition;
import com.rnkrsoft.framework.messagequeue.annotation.Selector;
import com.rnkrsoft.framework.messagequeue.annotation.SelectorType;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueListenerWrapper;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public abstract class AbstractMessageQueueConsumer implements MessageQueueConsumer {
    protected final List<MessageQueueListenerWrapper> listeners = new ArrayList();

    @Override
    public int registerListener(MessageQueueListener listener) {
        MessageQueueListenerWrapper listener0 = createListener(listener);
        listeners.add(listener0);
        return SUCCESS;
    }

    MessageQueueListenerWrapper createListener(MessageQueueListener listener) {
        Class clazz = listener.getClass();
        ListenerDefinition listenerDefinition = (ListenerDefinition) clazz.getAnnotation(ListenerDefinition.class);
        List<MessageQueueSelector> messageQueueSelectors = new ArrayList();
        boolean whenErrorRequeue = false;
        int maxTryProcessAge = 3;
        boolean ack = true;
        if (listenerDefinition == null) {
            //do nothing
        }else{
            Selector[] selectors = listenerDefinition.selectors();
            if (selectors.length == 0){//如果未配置选择器，则默认是*
                     messageQueueSelectors.add(new MessageQueueSelector(SelectorType.necessarily, "*"));
            }else {
                for (Selector selector : selectors) {
                    messageQueueSelectors.add(new MessageQueueSelector(selector.type(), selector.routingKey()));
                }
            }
            whenErrorRequeue = listenerDefinition.whenErrorRequeue();
            maxTryProcessAge = listenerDefinition.maxTryProcessAge();
            ack = listenerDefinition.ack();
        }
        MessageQueueListenerWrapper enhance = new MessageQueueListenerWrapper(listener, whenErrorRequeue, maxTryProcessAge, ack, messageQueueSelectors);
        return enhance;
    }
}
