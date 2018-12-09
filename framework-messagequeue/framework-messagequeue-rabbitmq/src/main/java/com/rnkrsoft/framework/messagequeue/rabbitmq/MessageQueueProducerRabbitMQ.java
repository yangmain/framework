package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;
import com.rnkrsoft.framework.messagequeue.producer.AbstractMessageQueueProducer;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by rnkrsoft.com on 2018/5/22.
 */
@Slf4j
public class MessageQueueProducerRabbitMQ extends AbstractMessageQueueProducer implements InitializingBean {
    Channel channel;
    Connection connection;

    @Setter
    String exchangeName;

    @Setter
    String uri;

    @Override
    public void setUsername(String username) {
        throw ErrorContextFactory.instance().message("不支持").runtimeException();
    }

    @Override
    public void setPassword(String password) {
        throw ErrorContextFactory.instance().message("不支持").runtimeException();
    }

    @Override
    public void init() {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置RabbitMQ地址
            //amqp://username:password@localhost:5671
            factory.setUri(uri);
            //创建一个新的连接
            connection = factory.newConnection();
            //创建一个通道
            channel = connection.createChannel();
            try {
                //检测交换区是否存在
                channel.exchangeDeclarePassive(exchangeName);
            } catch (Exception e) {
                if (e instanceof ShutdownSignalException) {
                    ShutdownSignalException shutdownSignalException = (ShutdownSignalException) e;
                    if (shutdownSignalException.getMessage().contains("reply-code=404")) {
                        log.error("rabbit producer init happens error!", e);
                        throw ErrorContextFactory.instance()
                                .message("rabbit producer init happens error!")
                                .solution("检查Rabbit MQ是否已启动")
                                .runtimeException();
                    } else {
                        log.error("rabbit producer init happens error!", e);
                        throw ErrorContextFactory.instance()
                                .message("rabbit producer init happens error!")
                                .solution("检查Rabbit MQ是否已启动")
                                .runtimeException();
                    }
                } else {
                    log.error("rabbit producer init happens error!", e);
                    throw ErrorContextFactory.instance()
                            .message("rabbit producer init happens error!")
                            .solution("检查Rabbit MQ是否已启动")
                            .runtimeException();
                }
            }
        } catch (Exception e) {
            log.error("rabbit producer init happens error!", e);
            throw ErrorContextFactory.instance()
                    .message("rabbit producer init happens error!")
                    .solution("检查Rabbit MQ是否已启动")
                    .runtimeException();
        }

    }

    @Override
    public void destroy() {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int produce(Message message) {
        try {
            //发送消息到队列中
            channel.basicPublish(exchangeName, message.getRoutingKey(), null, message.asJson().getBytes("UTF-8"));
        } catch (Exception e) {
            log.error("rabbit producer publish message  happens error!", e);
            throw ErrorContextFactory.instance()
                    .message("rabbit producer publish message  happens error!")
                    .solution("检查Rabbit MQ是否已启动")
                    .runtimeException();
        }
        return SUCCESS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
