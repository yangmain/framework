package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueProducer;
import lombok.Data;
import org.apache.activemq.ActiveMQConnection;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/5/27.
 */
public class MessageQueueProducerActiveMQTest {

    @Test
    public void testProduce() throws Exception {
        MessageQueueProducer producer = new MessageQueueProducerActiveMQ();
//        producer.setUri("tcp://221.5.140.21:6161");
        producer.setUri("failover:(tcp://221.5.140.21:6161)?Randomize=false");
        producer.setUsername(ActiveMQConnection.DEFAULT_USER);
        producer.setPassword(ActiveMQConnection.DEFAULT_PASSWORD);
        producer.init();
        Message message = new Message(new Bean("test1", 2));
        message.setRoutingKey("FirstQueue");
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