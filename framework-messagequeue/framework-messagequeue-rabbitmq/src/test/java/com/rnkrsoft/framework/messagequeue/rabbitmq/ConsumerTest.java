package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rabbitmq.client.*;
import com.rnkrsoft.logtrace4j.ErrorContextFactory;
import com.rnkrsoft.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by woate on 2018/12/8.
 */
@Slf4j
public class ConsumerTest {

    @Test
    public void test1() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ地址
        //amqp://username:password@localhost:5671
        factory.setUri("amqp://zxevpop:pro_123456@192.168.100.245:5672");
        //创建一个新的连接 构建一个线程池，隔离业务
        Connection connection = factory.newConnection(executorService);
        //创建一个通道
        final Channel channel = connection.createChannel();
        //consumerTag 消费者标签，用来区分多个消费者
        //noLocal 设置为true，表示 不能将同一个Conenction中生产者发送的消息传递给这个Connection中 的消费者
        //exclusive 是否排他
        //arguments 消费者的参数
        //callback 消费者 DefaultConsumer建立使用，重写其中的方法
        String queueName = "sdsds";
        channel.basicQos(1);
        channel.basicConsume(queueName, false, "", true, true, null, new Consumer() {
            @Override
            public void handleConsumeOk(String consumerTag) {

            }

            @Override
            public void handleCancelOk(String consumerTag) {
                //handleCancelOk方法会在其它方法之前调用，返回消费者标签 *handleCancelOk与handleCancel消费者可以显式或者隐式的取水订单的时候调用，也可以通过
                //channel.basicCancel方法来显式的取消一个消费者订阅会首先触发handleConsumeOk方法，之后触发handleDelivery方法，最后才触发handleCancelOk方法
            }

            @Override
            public void handleCancel(String consumerTag) throws IOException {

            }

            @Override
            public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                //handleShutdownSignal方法 当Channel与Conenction关闭的时候会调用，
            }

            @Override
            public void handleRecoverOk(String consumerTag) {

            }

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //处理监听的消息
               try {
                   System.out.println(DateUtils.getCurrFullTime());
                   System.out.println("getRoutingKey:" + envelope.getRoutingKey());
                   String body2 = new String(body);
                   System.out.println("body2:" + body2);

                   String messageIdentifier =properties.getCorrelationId();
                   System.out.println("messageIdentifier:" + messageIdentifier);
               }finally {
                   try {
                       channel.basicAck(envelope.getDeliveryTag(), false);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
               }


            }
        });
        //deliveryTag:该消息的index
        //multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息。
//        channel.basicAck(0l, false);
        //deliveryTag:该消息的index
        //multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
        //requeue：被拒绝的是否重新入队列
        //channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
        //deliveryTag:该消息的index
        //requeue：被拒绝的是否重新入队列
        //channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
        Thread.sleep(1000000);
        executorService.shutdownNow();
    }
}
