package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by woate on 2018/12/8.
 */
public class ProducerTest {
    @Test
    public void test1() throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ地址
        //amqp://username:password@localhost:5671
        factory.setUri("amqp://zxevpop:pro_123456@192.168.100.245:5672");
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        channel.exchangeDeclarePassive("xxx");
        channel.close();
        connection.close();
    }
    @Test
    public void test2() throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ地址
        //amqp://username:password@localhost:5671
        factory.setUri("amqp://zxevpop:pro_123456@192.168.100.245:5672");
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();

//        channel.exchangeDeclare("framework.test", "topic", true, false, null);
        //queue: 用来存储消息的队列名称
        //durable: 用于指定当服务器重启时,队列是否能复活.注意,当服务器重启时,如果要保证消息持久性,必须将队列声明为持久化的
        //exclusive:  是否为当前连接的专用队列，在连接断开后，会自动删除该队列，生产环境中应该很少用到吧。
        //autoDelete:当队列不再使用时,用于指示RabbitMQ broker是否要自动删除队列.
        //arguments: 这是一个可选的队列构建参数map.
//        channel.queueDeclare("queue.test", true, false, false, null);
//        channel.queueBind("queue.test", "framework.test","test.routingkey");
        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("framework.test", "test.routingkey", MessageProperties.PERSISTENT_TEXT_PLAIN, String.valueOf(i).getBytes("UTF-8"));
            Thread.sleep(100);
        }
        channel.close();
        connection.close();
    }
}
