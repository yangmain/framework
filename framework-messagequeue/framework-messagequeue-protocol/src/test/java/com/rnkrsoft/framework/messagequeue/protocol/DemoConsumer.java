package com.rnkrsoft.framework.messagequeue.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public class DemoConsumer implements MessageQueueConsumer{
    final List<MessageQueueListener> listeners = new ArrayList();
    @Override
    public int registerListener(MessageQueueListener listener) {
        listeners.add(listener);
        return SUCCESS;
    }

    @Override
    public int startup(ConsumerType type) {
        System.out.println("开始启动监听");
        List<Message> messages = new ArrayList<Message>();
        messages.add(new Message(new DemoObject("ssss", 1)));
        messages.add(new Message(new DemoObject("12", 1)));
        messages.add(new Message(new DemoObject("34", 1)));
        messages.add(new Message(new DemoObject("56", 1)));
        for (Message message : messages){
            for (MessageQueueListener listener : listeners){
                listener.execute(message);
            }
        }
        return SUCCESS;
    }

    @Override
    public int startup() {
        return startup(ConsumerType.HEAD);
    }

    @Override
    public int shutdown() {
        return 0;
    }
}
