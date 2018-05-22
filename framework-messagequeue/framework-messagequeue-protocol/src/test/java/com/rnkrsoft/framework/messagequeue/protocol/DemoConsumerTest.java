package com.rnkrsoft.framework.messagequeue.protocol;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/5/21.
 */
public class DemoConsumerTest {

    @Test
    public void testStartup() throws Exception {
        MessageQueueConsumer consumer = new DemoConsumer();
        consumer.registerListener(new MessageQueueListener<DemoObject>() {
            @Override
            public void execute(Message<DemoObject> message) {
                System.out.println(message.get().msg);
                System.out.println(message.age);
            }
        });
        consumer.startup();
    }
}