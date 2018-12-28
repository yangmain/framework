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
        consumerRabbitMQ.setUri("amqp://zxevpop:pro_123456@221.5.140.21:5672");
        consumerRabbitMQ.setAutoAck(true);
        consumerRabbitMQ.setUseNio(false);
        consumerRabbitMQ.setConsumeThreadNum(50);
        consumerRabbitMQ.setQueueName("evpop_queue");
        List<MessageQueueListener> listeners = new ArrayList();
        listeners.add(new MessageQueueListener<MessageQueueProducerRabbitMQTest.Bean>() {
            @Override
            public List<MessageQueueSelector> getSelectors() {
                return Arrays.asList(new MessageQueueSelector(SelectorType.fusing, "USER_LOGIN_EVENT"));
            }

            @Override
            public void execute(Message<MessageQueueProducerRabbitMQTest.Bean> message) {
                System.out.println(Thread.currentThread().getName() + ":" + message);
                throw new RuntimeException();
            }
        });
        consumerRabbitMQ.setMessageQueueListeners(listeners);
        consumerRabbitMQ.afterPropertiesSet();
        Thread.sleep(100000);
    }
}