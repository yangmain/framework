package com.rnkrsoft.framework.messagequeue.activemq;

import com.rnkrsoft.framework.messagequeue.consumer.AbstractMessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.jms.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Slf4j
public class MessageQueueConsumerActiveMQ extends AbstractMessageQueueConsumer implements InitializingBean {
    @Setter
    String uri;
    @Setter
    String username;
    @Setter
    String password;
    /**
     * 是否使用自动确认消息
     */
    @Setter
    boolean autoAck = false;

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
    protected void init() {
        if (uri == null || uri.isEmpty()) {
            throw ErrorContextFactory.instance()
                    .message("activeMQ consumer is not config uri!")
                    .solution("please set activeMQ uri")
                    .runtimeException();
        }
        // 实例化连接工厂
        try {
            this.connectionFactory = new ActiveMQConnectionFactory(username, password, new URI(uri));
            this.connection = connectionFactory.createConnection(); // 通过连接工厂获取连接
            this.connection.start(); // 启动连接
            this.session = this.connection.createSession(Boolean.FALSE, autoAck ? Session.AUTO_ACKNOWLEDGE : Session.CLIENT_ACKNOWLEDGE); // 创建Session
            if (log.isDebugEnabled()) {
                log.debug("consumer init...");
                log.debug("listeners {}", listeners.size());
            }
            if (messageQueueListeners != null) {
                for (MessageQueueListener listener : messageQueueListeners) {
                    registerListener(listener);
                }
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
                                listener.execute(message);
                                if (!autoAck){
                                    MessageQueueConsumerActiveMQ.this.session.commit();
                                }
                            } catch (JMSException e) {
                                log.error("转换MQ Message发生错误", e);
                            }

                        }
                    });
                }
            }
        } catch (Exception e) {
            log.error("activeMQ consumer init happens error!", e);
            throw ErrorContextFactory.instance()
                    .message("activeMQ consumer init happens error!")
                    .solution("检查activeMQ是否已启动")
                    .runtimeException();
        }
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
        init();
    }
}
