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
    Connection connection;

    @Setter
    String exchangeName;

    @Setter
    String uri;

    @Override
    public void setUsername(String username) {
        throw ErrorContextFactory.instance()
                .message("rabbitmq consumer is not supported username!")
                .solution("please set amqp://username:password@localhost:5672")
                .runtimeException();
    }

    @Override
    public void setPassword(String password) {
        throw ErrorContextFactory.instance()
                .message("rabbitmq consumer is not supported password!")
                .solution("please set amqp://username:password@localhost:5672")
                .runtimeException();
    }

    @Override
    public void init() {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置RabbitMQ地址
            //amqp://username:password@localhost:5671
            factory.setUri(uri);
            //设置网络异常重连
            factory.setAutomaticRecoveryEnabled(true);
            //设置 没10s ，重试一次
            factory.setNetworkRecoveryInterval(10 * 1000);
            //创建一个新的连接
            connection = factory.newConnection();
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
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int produce(Message message) {
        //创建一个通道
       Channel channel = null;
        try {
            channel = connection.createChannel();
            //检测交换区是否存在
            channel.exchangeDeclarePassive(exchangeName);
            //发送消息到队列中
            channel.basicPublish(exchangeName, message.getRoutingKey(), null, message.asJson().getBytes("UTF-8"));
        } catch (Exception e) {
            if (e instanceof ShutdownSignalException) {
                ShutdownSignalException shutdownSignalException = (ShutdownSignalException) e;
                if (shutdownSignalException.getMessage().contains("reply-code=404")) {
                    throw ErrorContextFactory.instance()
                            .message("rabbitmq exchange is not defined!")
                            .solution("在服务器端定义交换区'{}'", exchangeName)
                            .cause(e)
                            .runtimeException();
                } else {
                    throw ErrorContextFactory.instance()
                            .message("rabbitmq producer happens error!")
                            .solution("检查rabbitmq相关配置")
                            .cause(e)
                            .runtimeException();
                }
            } else {
                throw ErrorContextFactory.instance()
                        .message("rabbitmq producer happens error!")
                        .solution("检查rabbitmq相关配置")
                        .cause(e)
                        .runtimeException();
            }
        }finally {
            if (channel != null){
                try {
                    channel.close();
                } catch (Exception e) {
                    log.warn("close channel happens error!", e);
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
