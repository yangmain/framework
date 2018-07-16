package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.MessageQueueProducerFactory;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueProducer;
import lombok.Data;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/27.
 */
public class MessageQueueProducerActiveMQTest {

    @Test
    public void testProduce() throws Exception {
        MessageQueueProducer producer = new MessageQueueProducerActiveMQ();
        producer.setUrl("tcp://127.0.0.1:61616");
        producer.setUsername("admin");
        producer.setPassword("admin");
        producer.init();
        Message message = new Message(new Bean("test1", 2));
        message.setRoutingKey("FirstQueue1");
        producer.produce(message);
    }


  public static class Bean{
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
}