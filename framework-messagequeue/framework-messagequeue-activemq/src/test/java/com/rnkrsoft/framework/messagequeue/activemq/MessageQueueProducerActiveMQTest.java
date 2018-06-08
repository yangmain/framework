package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.MessageQueueProducerFactory;
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
        MessageQueueProducer producer = MessageQueueProducerFactory.getInstance();
        producer.setUrl("xxxxx");
        producer.setUsername("demo");
        producer.setPassword("sdada");
        producer.init();
        producer.produce(new Bean("test1", 2));
    }


   static class Bean{
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