package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.impl.nio.NioParams;
import com.rnkrsoft.framework.messagequeue.consumer.AbstractMessageQueueConsumer;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Slf4j
public class MessageQueueConsumerRabbitMQ extends AbstractMessageQueueConsumer implements InitializingBean {
    @Setter
    String uri;
    /**
     * 是否使用自动确认消息
     */
    @Setter
    boolean autoAck = false;

    @Setter
    boolean useNio = false;

    Connection connection;

    Channel channel;

    @Setter
    String queueName;

    @Setter
    int consumeThreadNum = 10;

    /**
     * 通过Spring配置进行监听器注册
     */
    @Setter
    List<MessageQueueListener> messageQueueListeners;

    @Override
    protected void init() {
        if (uri == null || uri.isEmpty()) {
            throw ErrorContextFactory.instance()
                    .message("rabbitmq consumer is not config uri!")
                    .solution("please set amqp://username:password@localhost:5672")
                    .runtimeException();
        }
        if (queueName == null || queueName.isEmpty()) {
            throw ErrorContextFactory.instance()
                    .message("rabbitmq consumer is not config queue name!")
                    .solution("please set queue name")
                    .runtimeException();
        }
        if (messageQueueListeners == null || messageQueueListeners.isEmpty()) {
            throw ErrorContextFactory.instance()
                    .message("rabbitmq consumer is not config messageQueueListeners!")
                    .solution("please set message Queue Listeners")
                    .runtimeException();
        }
        for (MessageQueueListener listener : messageQueueListeners) {
            registerListener(listener);
        }
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置RabbitMQ地址
            factory.setUri(uri);
            //设置网络异常重连
            factory.setAutomaticRecoveryEnabled(true);
            //设置 10s ，重试一次
            factory.setNetworkRecoveryInterval(10000);
            if (useNio) {
                factory.useNio();
                factory.setNioParams(new NioParams().setNbIoThreads(4));
            }
            //创建一个新的连接
            connection = factory.newConnection(Executors.newFixedThreadPool(consumeThreadNum));
            //创建一个通道
            channel = connection.createChannel();
        } catch (Exception e) {
            throw ErrorContextFactory.instance()
                    .message("rabbitmq consumer init happens error!")
                    .solution("检查Rabbit MQ是否已启动")
                    .cause(e)
                    .runtimeException();
        }
        if (log.isDebugEnabled()) {
            log.debug("consumer init...");
        }
        try {
            channel.queueDeclarePassive(queueName);
        } catch (Exception e) {
            if (e.getCause() instanceof ShutdownSignalException) {
                ShutdownSignalException shutdownSignalException = (ShutdownSignalException) e.getCause();
                if (shutdownSignalException.getMessage().contains("reply-code=404")) {
                    throw ErrorContextFactory.instance()
                            .message("rabbitmq queue '{}' is not exists!", queueName)
                            .solution("确认MQ是否定义了队列 {}", queueName)
                            .cause(e)
                            .runtimeException();
                } else {
                    throw ErrorContextFactory.instance()
                            .message("rabbitmq consume init happens error!")
                            .solution("检查Rabbit MQ是否已启动")
                            .cause(e)
                            .runtimeException();
                }
            } else {
                throw ErrorContextFactory.instance()
                        .message("rabbitmq consume happens error!")
                        .solution("检查Rabbit MQ是否已启动")
                        .cause(e)
                        .runtimeException();
            }
        }
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        try {
            //每次从队列获取的数量
            channel.basicQos(1);
            //启用ACK事务 autoAck 为真，表示无事务
            channel.basicConsume(queueName, autoAck, new RabbitMQConsumeCallback(channel, autoAck, listeners));
        } catch (Exception e) {
            if (e.getCause() instanceof ShutdownSignalException) {
                ShutdownSignalException shutdownSignalException = (ShutdownSignalException) e.getCause();
                if (shutdownSignalException.getMessage().contains("reply-code=404")) {
                    throw ErrorContextFactory.instance()
                            .message("rabbitmq queue '{}' is not exists!", queueName)
                            .solution("确认MQ是否定义了队列 {}", queueName)
                            .cause(e)
                            .runtimeException();
                } else {
                    throw ErrorContextFactory.instance()
                            .message("rabbitmq consume happens error!")
                            .solution("检查Rabbit MQ是否已启动")
                            .cause(e)
                            .runtimeException();
                }
            }
            throw ErrorContextFactory.instance()
                    .message("rabbit consumer happens error!")
                    .solution("检查Rabbit MQ是否已启动")
                    .cause(e)
                    .runtimeException();
        }
    }


    @Override
    public int shutdown() {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.warn("shutdown rabbitmq happens error!", e);
        }
        return SUCCESS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ErrorContextFactory.instance().reset();
        startup();
        ErrorContextFactory.instance().reset();
    }
}
