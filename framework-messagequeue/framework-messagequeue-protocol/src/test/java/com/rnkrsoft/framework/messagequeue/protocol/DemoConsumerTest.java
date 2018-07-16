package com.rnkrsoft.framework.messagequeue.protocol;

import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public class DemoConsumerTest {

    @Test
    public void testStartup() throws Exception {
        MessageQueueConsumer consumer = new DemoConsumer();
        consumer.registerListener(new MessageQueueListener<DemoObject>() {
            @Override
            public List<MessageQueueSelector> getSelectors() {
                return null;
            }

            @Override
            public void execute(Message<DemoObject> message) {
                System.out.println(message.get().msg);
                System.out.println(message.age);
            }
        });
        consumer.startup();
    }
}