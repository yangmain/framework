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

    @com.rnkrsoft.framework.messagequeue.annotation.Message(routingKey = "USER_LOGIN_EVENT")
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
        messageQueueProducerRabbitMQ.setUri("amqp://zxevpop:pro_123456@221.5.140.21:5672");
        messageQueueProducerRabbitMQ.setExchangeName("evpop");
        messageQueueProducerRabbitMQ.init();

        for (int i = 0; i < 1000; i++) {
            Bean bean1 = new Bean(UUID.randomUUID().toString(), i);
            try {
                messageQueueProducerRabbitMQ.produce(bean1);
            }catch (Exception e){

            }
            Thread.sleep(1);
        }
    }
}