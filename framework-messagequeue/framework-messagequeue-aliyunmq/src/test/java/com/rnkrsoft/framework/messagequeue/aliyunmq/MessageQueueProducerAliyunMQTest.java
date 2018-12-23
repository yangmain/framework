package com.rnkrsoft.framework.messagequeue.aliyunmq;

import com.rnkrsoft.framework.messagequeue.protocol.Message;
import org.junit.Test;

/**
 * Created by rnkrsoft.com on 2018/7/13.
 */
public class MessageQueueProducerAliyunMQTest {

    @Test
    public void testInit() throws Exception {
        MessageQueueProducerAliyunMQ producer = new MessageQueueProducerAliyunMQ();
        producer.setUri("http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        producer.setAccessKey("LTAIMlDAW0HgWFGy");
        producer.setSecretKey("GO50lnzm2WvQv78GSv8Ud551XmXMGv");
        producer.setProducerId("PID_test_evpop");
        producer.setTopic("test_evpop");
        producer.init();
        producer.produce(new Bean("this is a test"));
    }

    @com.rnkrsoft.framework.messagequeue.annotation.Message(routingKey = "MEMBER_LOEIN_EVENT")
    public static class Bean{
        String name;

        public Bean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}