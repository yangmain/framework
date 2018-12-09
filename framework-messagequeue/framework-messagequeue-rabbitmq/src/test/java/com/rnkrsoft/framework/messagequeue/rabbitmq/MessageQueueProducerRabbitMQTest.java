package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import lombok.Data;
import org.junit.Test;

/**
 * Created by woate on 2018/12/8.
 */
public class MessageQueueProducerRabbitMQTest {

    @Data
    static class Bean1 {
        String name;
        int age;
    }

    @Test
    public void testProduce() throws Exception {
        MessageQueueProducerRabbitMQ messageQueueProducerRabbitMQ = new MessageQueueProducerRabbitMQ();
        messageQueueProducerRabbitMQ.setUri("amqp://zxevpop:pro_123456@192.168.100.245:5672");
        messageQueueProducerRabbitMQ.setExchangeName("framework.test");
        messageQueueProducerRabbitMQ.init();
        Bean1 bean1 = new Bean1();
        bean1.age = 2;
        bean1.setName("xsxsxsx");
        Message message = new Message(bean1);
        message.setRoutingKey("test.routingkey");
        for (int i = 0; i < 10000; i++) {
            messageQueueProducerRabbitMQ.produce(message);
        }
    }
}