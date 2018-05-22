package com.rnkrsoft.framework.messagequeue.protocol;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public enum ConsumerType {
    /**
     * 根据消息队列实现，自动识别
     */
    AUTO,
    /**
     * 从消息队列头开始消费
     */
    HEAD,
    /**
     * 从消息队列尾开始消费
     */
    TAIL
}
