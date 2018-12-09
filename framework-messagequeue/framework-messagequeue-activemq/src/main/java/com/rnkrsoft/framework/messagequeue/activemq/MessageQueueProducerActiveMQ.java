package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import lombok.Data;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.URI;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
public class MessageQueueProducerActiveMQ extends AbstractMessageQueueProducer {
    @Setter
    String uri;
    /**
     * 是否使用自动确认消息
     */
    @Setter
    boolean autoAck = false;


    Connection connection = null; // 连接
    Session session; // 会话 接受或者发送消息的线程



    @Override
    public void setUsername(String username) {
        throw new UnsupportedOperationException("不支持username");
    }

    @Override
    public void setPassword(String password) {
        throw new UnsupportedOperationException("不支持password");
    }

    public void init() {
        // 实例化连接工厂
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(new URI(uri));
            this.connection = connectionFactory.createConnection(); // 通过连接工厂获取连接
            this.connection.start(); // 启动连接
            this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            this.session.close();
            this.connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int produce(Message message) {
        Destination destination; // 消息的目的地
        MessageProducer messageProducer; // 消息生产者
        try {
            destination = this.session.createTopic(message.getRoutingKey()); // 创建消息队列
            messageProducer = this.session.createProducer(destination); // 创建消息生产者
            TextMessage textMessage = this.session.createTextMessage();
            textMessage.setText(message.asJson());
            messageProducer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}
