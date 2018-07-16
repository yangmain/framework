package com.rnkrsoft.framework.messagequeue.aliyunmq;

import com.rnkrsoft.framework.messagequeue.annotation.SelectorType;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.AbstractMessageQueueListener;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by woate on 2018/7/13.
 */
@Slf4j
public class MessageQueueConsumerAliyunMQTest {

    @Test
    public void testStartup() throws Exception {
        MessageQueueConsumerAliyunMQ consumer = new MessageQueueConsumerAliyunMQ();
        consumer.setUrl("http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        consumer.setAccessKey("LTAI5x0QB7HAnY4T");
        consumer.setSecretKey("NnXaoXaSP32NQc4wwAKJUD4W2qNV0g");
        consumer.setConsumerId("CID_test_evpop");
        consumer.setConsumeThreadNum(4);
        consumer.setTopic("test_evpop");
        consumer.registerListener(new AbstractMessageQueueListener(Arrays.asList(new MessageQueueSelector(SelectorType.fusing, "test_1"))) {
            @Override
            public void execute(Message message) {
                log.info(message.asJson());
                log.info("age : '{}'",message.getAge());
                log.info("routingKey: '{}'", message.getRoutingKey());
            }
        });
        consumer.startup();
        Thread.sleep(60 * 1000);
    }
}