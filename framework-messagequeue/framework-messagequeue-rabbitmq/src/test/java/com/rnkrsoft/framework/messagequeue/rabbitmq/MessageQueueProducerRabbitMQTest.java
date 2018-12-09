package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/12/8.
 */
public class MessageQueueProducerRabbitMQTest {

    @Test
    public void testProduce() throws Exception {
        MessageQueueProducerRabbitMQ messageQueueProducerRabbitMQ = new MessageQueueProducerRabbitMQ();
        messageQueueProducerRabbitMQ.setUrl("amqp://zxevpop:pro_123456@192.168.100.245:5672");
        messageQueueProducerRabbitMQ.setExchangeName("framework.test");
        messageQueueProducerRabbitMQ.init();
        Message message = new Message("xxxxxxxxxxxxxxxx");
        message.setRoutingKey("test.routingkey");
        for (int i = 0; i < 10000; i++) {
            messageQueueProducerRabbitMQ.produce(message);
        }
    }
}