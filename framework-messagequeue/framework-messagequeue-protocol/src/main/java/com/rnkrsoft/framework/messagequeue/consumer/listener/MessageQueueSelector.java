package com.rnkrsoft.framework.messagequeue.consumer.listener;

import com.rnkrsoft.framework.messagequeue.annotation.SelectorType;
import lombok.Getter;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
@Getter
public class MessageQueueSelector {
    SelectorType type = SelectorType.fusing;
    String routingKey;

    public boolean accept(String routingKey){
        return this.routingKey.equals(routingKey);
    }

    public MessageQueueSelector(SelectorType type, String routingKey) {
        this.type = type;
        this.routingKey = routingKey;
    }
}
