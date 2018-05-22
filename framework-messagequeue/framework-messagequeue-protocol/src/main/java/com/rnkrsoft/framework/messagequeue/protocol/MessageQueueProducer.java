package com.rnkrsoft.framework.messagequeue.protocol;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 * 消息队列生产者
 */
public interface MessageQueueProducer {
    int SUCCESS = 0;
    int FAILURE = -1;
    /**
     * 生产一个消息
     * @param message 消息对象
     * @return 执行结果
     */
    int produce(Message message);

    /**
     * 生产一个对象作为消息，自动进行包装
     * @param bean 一个Java Bean
     * @return 执行结果
     */
    int produce(Object bean);
}
