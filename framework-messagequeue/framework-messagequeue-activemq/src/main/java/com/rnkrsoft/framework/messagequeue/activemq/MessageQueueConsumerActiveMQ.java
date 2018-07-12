package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.consumer.AbstractMessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.protocol.ConsumerType;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
public class MessageQueueConsumerActiveMQ extends AbstractMessageQueueConsumer {
    String userName;
    String password;
    String brokeUrl;
    ConnectionFactory connectionFactory; // 连接工厂
    Connection connection = null; // 连接
    Session session; // 会话 接受或者发送消息的线程

    @Override
    public int startup(ConsumerType type) {
        // 实例化连接工厂
        this.connectionFactory = new ActiveMQConnectionFactory(userName, password, brokeUrl);
        try {
            this.connection = connectionFactory.createConnection(); // 通过连接工厂获取连接
            this.connection.start(); // 启动连接
            this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
            this.session.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(javax.jms.Message msg) {
                    BytesMessage bytesMessage = (BytesMessage) msg;
                    long length = 0;
                    try {
                        length = bytesMessage.getBodyLength();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    byte[] bytes = new byte[(int) length];
                    Message message = Message.message(bytes);
                    for (MessageQueueListener listener : listeners) {
                        listener.execute(message);
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    @Override
    public int startup() {
        return startup(ConsumerType.HEAD);
    }

    @Override
    public int shutdown() {
        try {
            this.session.close();
            this.session = null;
            return SUCCESS;
        } catch (JMSException e) {
            e.printStackTrace();
            return FAILURE;
        }
    }
}
