package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.jms.*;
import java.net.URI;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Slf4j
public class MessageQueueProducerActiveMQ extends AbstractMessageQueueProducer implements InitializingBean{
    @Setter
    String uri;
    /**
     * 是否使用自动确认消息
     */
    @Setter
    boolean autoAck = false;

    Connection connection = null; // 连接

    @Setter
    String username;
    @Setter
    String password;

    public void init() {
        // 实例化连接工厂
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, new URI(uri));
            this.connection = connectionFactory.createConnection(); // 通过连接工厂获取连接
            this.connection.start(); // 启动连接
        } catch (Exception e) {
            log.error("activeMQ consumer init happens error!", e);
            throw ErrorContextFactory.instance()
                    .message("activeMQ consumer init happens error!")
                    .solution("检查activeMQ是否已启动")
                    .runtimeException();
        }
        log.info("init activeMQ producer finished!");
    }

    @Override
    public void destroy() {
        try {
            this.connection.close();
        } catch (JMSException e) {
            log.error("activeMQ producer destroy happens error!", e);
            throw ErrorContextFactory.instance()
                    .message("activeMQ producer destroy happens error!")
                    .runtimeException();
        }
    }

    @Override
    public int produce(Message message) {
        Destination destination; // 消息的目的地
        MessageProducer messageProducer; // 消息生产者
        Session session = null;
        try {
            session = this.connection.createSession(Boolean.FALSE, autoAck ? Session.AUTO_ACKNOWLEDGE : Session.CLIENT_ACKNOWLEDGE); // 创建Session
            destination = session.createTopic(message.getRoutingKey()); // 创建消息队列
            messageProducer = session.createProducer(destination); // 创建消息生产者
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(message.asJson());
            messageProducer.send(textMessage);
        } catch (JMSException e) {
            log.error("activeMQ producer happens error!", e);
            return FAILURE;
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    log.error("activeMQ producer close session happens error!", e);
                    throw ErrorContextFactory.instance()
                            .message("activeMQ producer close session happens error!")
                            .runtimeException();
                }
            }
        }
        return SUCCESS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ErrorContextFactory.instance().reset();
        init();
        ErrorContextFactory.instance().reset();
    }
}
