package com.rnkrsoft.framework.messagequeue.rabbitmq;

import com.rabbitmq.client.*;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueListenerWrapper;
import com.rnkrsoft.framework.messagequeue.consumer.listener.MessageQueueSelector;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import com.rnkrsoft.message.MessageFormatter;
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
class RabbitMQConsumeCallback implements Consumer {
    Channel channel;
    /**
     * 是否使用自动确认消息
     */
    boolean autoAck = false;

    List<MessageQueueListenerWrapper> listeners;

    public RabbitMQConsumeCallback(Channel channel, boolean autoAck, List<MessageQueueListenerWrapper> listeners) {
        this.channel = channel;
        this.autoAck = autoAck;
        this.listeners = listeners;
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        if (log.isDebugEnabled()) {
            log.debug("consume consumerTag:'{}' success!", consumerTag);
        }
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        if (log.isDebugEnabled()) {
            log.debug("consume consumerTag:'{}' failure!", consumerTag);
        }
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        if (log.isDebugEnabled()) {
            log.debug("recover consumerTag:'{}' success!", consumerTag);
        }
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
        if (log.isDebugEnabled()) {
            log.debug("delivery consumerTag:'{}'", consumerTag);
        }
        String json = null;
        try {
            json = new String(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //无效的消息
            log.error("receive message is illegal character encoding!", e);
            if (!autoAck) {
                log.error("因为手工ack,将错误信息丢弃处理", e);
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
            log.error("routingKey '{}' is not found listener!", routingKey);
            return;
        }
        Message message = null;
        try {
            message = Message.message(json);
        } catch (Exception e) {
            //无效的消息格式
            log.error("receive message is illegal!", e);
            if (!autoAck) {
                log.error("因为手工ack,将错误信息丢弃处理", e);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
            return;
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug(" handle '{}'", message);
            }
            listener.execute(message);
            if (!autoAck) {
                if (log.isDebugEnabled()){
                    log.error("因为手工ack,将成功处理的信息进行ack确认");
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        } catch (Throwable e) {
            log.error(MessageFormatter.format("consume message '{}' happens error!", message), e);
            if (!autoAck) {
                log.error("因为手工ack,将错误信息丢弃处理", e);
                channel.basicNack(envelope.getDeliveryTag(), false, false);
            }
        }
    }
}
