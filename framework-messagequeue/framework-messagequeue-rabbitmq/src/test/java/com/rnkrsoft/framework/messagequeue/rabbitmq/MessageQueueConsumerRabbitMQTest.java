package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rnkrsoft.framework.messagequeue.annotation.SelectorType;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/12/8.
 */
public class MessageQueueConsumerRabbitMQTest {

    @Test
    public void testInit() throws Exception {
        MessageQueueConsumerRabbitMQ consumerRabbitMQ = new MessageQueueConsumerRabbitMQ();
        consumerRabbitMQ.setUrl("amqp://zxevpop:pro_123456@192.168.100.245:5672");
        consumerRabbitMQ.setAutoAck(false);
        consumerRabbitMQ.setMaxThreadNum(50);
        consumerRabbitMQ.setQueueName("queue.test");
        List<MessageQueueListener> listeners = new ArrayList();
        listeners.add(new MessageQueueListener() {
            @Override
            public List<MessageQueueSelector> getSelectors() {
                return Arrays.asList(new MessageQueueSelector(SelectorType.fusing, "test.routingkey"));
            }

            @Override
            public void execute(Message message) {
                System.out.println(Thread.currentThread().getName() + ":" + message);
            }
        });
        consumerRabbitMQ.setMessageQueueListeners(listeners);
        consumerRabbitMQ.afterPropertiesSet();
        Thread.sleep(100000);
    }
}