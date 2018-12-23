package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rnkrsoft.framework.messagequeue.annotation.Selector;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import lombok.Data;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by woate on 2018/12/8.
 */
public class MessageQueueProducerRabbitMQTest {

    @com.rnkrsoft.framework.messagequeue.annotation.Message(routingKey = "test.routingkey")
    public static class Bean {
        String name;
        int age;

        public Bean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
    @Test
    public void testProduce() throws Exception {
        MessageQueueProducerRabbitMQ messageQueueProducerRabbitMQ = new MessageQueueProducerRabbitMQ();
        messageQueueProducerRabbitMQ.setUri("amqp://zxevpop:pro_123456@192.168.100.245:5672");
        messageQueueProducerRabbitMQ.setExchangeName("framework.test");
        messageQueueProducerRabbitMQ.init();

        for (int i = 0; i < 10000; i++) {
            Bean bean1 = new Bean(UUID.randomUUID().toString(), i);
            try {
                messageQueueProducerRabbitMQ.produce(bean1);
            }catch (Exception e){

            }
            Thread.sleep(1);
        }
    }
}