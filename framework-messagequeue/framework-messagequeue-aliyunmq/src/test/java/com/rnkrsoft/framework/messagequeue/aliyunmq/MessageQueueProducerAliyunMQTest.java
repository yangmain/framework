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
        producer.setUrl("http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        producer.setAccessKey("LTAI5x0QB7HAnY4T");
        producer.setSecretKey("NnXaoXaSP32NQc4wwAKJUD4W2qNV0g");
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