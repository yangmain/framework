package com.rnkrsoft.framework.messagequeue.protocol;

import com.rnkrsoft.framework.messagequeue.annotation.Selector;
import com.rnkrsoft.framework.messagequeue.annotation.SelectorType;
import com.rnkrsoft.framework.messagequeue.annotation.ListenerDefinition;

/**
 * Created by woate on 2018/5/21.
 */
@ListenerDefinition(selectors = {
        @Selector(type = SelectorType.fusing, routingKey = "test")
})
public class DemoListener implements MessageQueueListener<DemoObject>{
    @Override
    public void execute(Message<DemoObject> message) {
        DemoObject demoObject = message.get();
        if (message.age > 3){
            return;
        }
        System.out.println(demoObject);
        System.out.println(message.createDate);
        System.out.println(message.lastUpdateDate);
    }
}
