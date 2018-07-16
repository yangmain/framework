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
 * Created by woate on 2018/7/13.
 */
@Slf4j
public class MessageQueueConsumerActiveMQTest {

    @Test
    public void testStartup() throws Exception {
        MessageQueueConsumerActiveMQ consumer = new MessageQueueConsumerActiveMQ();
        consumer.setUrl(ActiveMQConnection.DEFAULT_BROKER_URL);
        consumer.setUsername(ActiveMQConnection.DEFAULT_USER);
        consumer.setPassword(ActiveMQConnection.DEFAULT_PASSWORD);
        consumer.registerListener(new MessageQueueListener() {
            @Override
            public List<MessageQueueSelector> getSelectors() {
                return new ArrayList<MessageQueueSelector>(Arrays.asList(new MessageQueueSelector(SelectorType.fusing, "FirstQueue1")));
            }

            @Override
            public void execute(Message message) {
                log.debug("----------" +message.get());
            }
        });
        consumer.startup();
        Thread.sleep(60 * 1000);
    }
}