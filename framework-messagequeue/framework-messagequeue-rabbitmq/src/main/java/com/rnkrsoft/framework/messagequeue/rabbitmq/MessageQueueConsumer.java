package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rabbitmq.client.*;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueListenerWrapper;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by woate on 2018/12/8.
 */
@Slf4j
@Data
class MessageQueueConsumer implements Consumer{
    Channel channel;
    /**
     * 是否使用自动确认消息
     */
    boolean autoAck = false;

    List<MessageQueueListenerWrapper> listeners;

    public MessageQueueConsumer(Channel channel, boolean autoAck, List<MessageQueueListenerWrapper> listeners) {
        this.channel = channel;
        this.autoAck = autoAck;
        this.listeners = listeners;
    }

    @Override
    public void handleConsumeOk(String consumerTag) {

    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }


    MessageQueueListener lookupListener(String routingKey) {
        for (MessageQueueListener listener : listeners) {
            List<MessageQueueSelector> selectors = listener.getSelectors();
            for (MessageQueueSelector selector : selectors) {
                if (selector.accept(routingKey)) {
                    return listener;
                }
            }
        }
        return null;
    }
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String json = null;
        try {
            json = new String(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //无效的消息
            log.error("receive message happen error!", e);
            if (!autoAck) {
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
            return;
        }
        String routingKey = envelope.getRoutingKey();
        if (log.isDebugEnabled()) {
            log.debug("receive message: '{}' routingKey:'{}' deliveryTag:'{}'", json, routingKey, envelope.getDeliveryTag());
        }
        MessageQueueListener listener = lookupListener(routingKey);
        if (listener == null) {
            return;
        }
        Message message = null;
        try {
            message = Message.message(json);
        } catch (Exception e) {
            //无效的消息格式
            log.error("receive message is illegal!", e);
            if (!autoAck) {
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug(" handle '{}'", message);
        }
        try {
            listener.execute(message);
        } catch (Exception e) {
            if (!autoAck) {
                channel.basicNack(envelope.getDeliveryTag(), false, false);
            }
        } finally {
            if (!autoAck) {
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        }
    }
}
