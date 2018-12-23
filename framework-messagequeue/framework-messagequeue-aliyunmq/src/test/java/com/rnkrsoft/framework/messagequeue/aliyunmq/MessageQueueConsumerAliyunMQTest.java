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
 * Created by rnkrsoft.com on 2018/7/13.
 */
@Slf4j
public class MessageQueueConsumerAliyunMQTest {

    @Test
    public void testStartup() throws Exception {
        MessageQueueConsumerAliyunMQ consumer = new MessageQueueConsumerAliyunMQ();
        consumer.setUri("http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        consumer.setAccessKey("LTAIKDX2i8zh3ePu");
        consumer.setSecretKey("8pNr17TsrrVKmgw2jzlpXuAS62gzHf");
        consumer.setConsumerId("CID_test_evpop");
        consumer.setTopic("test_evpop");
        consumer.setConsumeThreadNum(4);
        consumer.setMessageQueueListeners(Arrays.<MessageQueueListener>asList(new AbstractMessageQueueListener(Arrays.asList(new MessageQueueSelector(SelectorType.fusing, "MEMBER_LOEIN_EVENT"))) {
            @Override
            public void execute(Message message) {
                log.info(message.asJson());
                log.info("age : '{}'",message.getAge());
                log.info("MEMBER_LOEIN_EVENT: '{}'", message.getRoutingKey());
                Object object = message.get();
                System.out.println(object.getClass());
                MessageQueueProducerAliyunMQTest.Bean bean = (MessageQueueProducerAliyunMQTest.Bean)object;
                System.out.println(bean.getName());
            }
        },new AbstractMessageQueueListener(Arrays.asList(new MessageQueueSelector(SelectorType.fusing, "register"))) {
            @Override
            public void execute(Message message) {
                log.info("register user");
                log.info(message.asJson());
                log.info("age : '{}'",message.getAge());
                log.info("routingKey: '{}'", message.getRoutingKey());
            }
        }));
        consumer.afterPropertiesSet();
        Thread.sleep(600 * 1000);
    }
}