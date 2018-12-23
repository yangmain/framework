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
        producer.setAccessKey("LTAIdme6vRelL885");
        producer.setSecretKey("R1cnDVY42NOALqBk8V5HKUmPGhAeG3");
        producer.setProducerId("PID_test_evpop");
        producer.setTopic("test_evpop");
        producer.init();
        for (int i = 0; i < 1000 ; i++) {
            producer.produce(new Bean("this is a test" + i));
            producer.produce(new Bean1("register" + i));
        }
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

    @com.rnkrsoft.framework.messagequeue.annotation.Message(routingKey = "register")
    public static class Bean1{
        String name;

        public Bean1(String name) {
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