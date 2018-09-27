package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.consumer.AbstractMessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.ConsumerType;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Slf4j
public class MessageQueueConsumerActiveMQ extends AbstractMessageQueueConsumer implements InitializingBean {
    @Setter
    String username;
    @Setter
    String password;
    @Setter
    String url;
    /**
     * 连接工厂
     */
    ConnectionFactory connectionFactory;
    /**
     * 连接
     */
    Connection connection = null;
    /**
     * 会话
     */
    Session session;
    final List<MessageConsumer> consumers = new ArrayList();
    /**
     * 通过Spring配置进行监听器注册
     */
    @Setter
    List<MessageQueueListener> messageQueueListeners;

    @Override
    public int startup(ConsumerType type) {
        // 实例化连接工厂
        this.connectionFactory = new ActiveMQConnectionFactory(username, password, url);
        try {
            this.connection = connectionFactory.createConnection(); // 通过连接工厂获取连接
            this.connection.start(); // 启动连接
            this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
            if (log.isDebugEnabled()) {
                log.debug("consumer init...");
                log.debug("listeners {}", listeners.size());
            }
            for (final MessageQueueListener listener : listeners) {
                List<MessageQueueSelector> selectors = listener.getSelectors();
                for (MessageQueueSelector selector : selectors) {
                    log.info(" create consumer routingKey :'{}'", selector.getRoutingKey());
                    MessageConsumer consumer = this.session.createConsumer(this.session.createTopic(selector.getRoutingKey()));
                    consumers.add(consumer);
                    consumer.setMessageListener(new MessageListener() {
                        @Override
                        public void onMessage(javax.jms.Message msg) {
                            TextMessage textMessage = (TextMessage) msg;
                            Message message = null;
                            try {
                                if (log.isDebugEnabled()) {
                                    log.debug("receive message: '{}'", textMessage.getText());
                                }
                                message = Message.message(textMessage.getText());
                            } catch (JMSException e) {
                               log.error("转换MQ Message发生错误", e);
                            }
                            listener.execute(message);
                        }
                    });
                }
            }
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
            for (MessageConsumer consumer : consumers) {
                consumer.close();
            }
            this.session.close();
            this.session = null;
            return SUCCESS;
        } catch (JMSException e) {
            e.printStackTrace();
            return FAILURE;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (messageQueueListeners != null){
            for (MessageQueueListener listener : messageQueueListeners){
                registerListener(listener);
            }
        }
    }
}
