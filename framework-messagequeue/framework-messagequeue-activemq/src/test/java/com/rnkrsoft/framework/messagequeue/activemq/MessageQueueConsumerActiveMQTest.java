package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.annotation.SelectorType;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rnkrsoft.com on 2018/7/13.
 */
@Slf4j
public class MessageQueueConsumerActiveMQTest {

    @Test
    public void testStartup() throws Exception {
        MessageQueueConsumerActiveMQ consumer = new MessageQueueConsumerActiveMQ();
        consumer.setUri("failover:(tcp://221.5.140.21:6161)?Randomize=false");
        consumer.setUsername(ActiveMQConnection.DEFAULT_USER);
        consumer.setPassword(ActiveMQConnection.DEFAULT_PASSWORD);
        consumer.setAutoAck(true);
        consumer.setMessageQueueListeners(Arrays.<MessageQueueListener>asList(new MessageQueueListener() {
            @Override
            public List<MessageQueueSelector> getSelectors() {
                return Arrays.asList(new MessageQueueSelector(SelectorType.fusing, "test.routingkey"));
            }

            @Override
            public void execute(Message message) {
                log.debug("----------" +message.get());
                MessageQueueProducerActiveMQTest.Bean bean = (MessageQueueProducerActiveMQTest.Bean)message.get();
                log.info("name:{}", bean.getName());
                log.info("age:{}", bean.getAge());
                if (bean.getAge() % 3 ==0){
                    throw new RuntimeException();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        consumer.afterPropertiesSet();
        Thread.sleep(600 * 1000);
    }
}
