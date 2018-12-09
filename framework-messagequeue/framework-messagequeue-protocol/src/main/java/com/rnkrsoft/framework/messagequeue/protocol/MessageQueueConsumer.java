package com.rnkrsoft.framework.messagequeue.protocol;

/**
 * Created by rnkrsoft on 2018/5/21.
 * 消息队列消费者
 */
public interface MessageQueueConsumer {
    int SUCCESS = 0;
    int FAILURE = -1;

    /**
     * 注册监听器
     * @param listener 监听器
     * @return 返回注册结果
     */
    int registerListener(MessageQueueListener listener);

    /**
     * 启动监听
     * @param type 消费类型
     * @return 执行结果
     */
    int startup(ConsumerType type);
    /**
     * 启动监听
     * @return 执行结果
     */
    int startup();

    /**
     * 关闭监听
     * @return 执行结果
     */
    int shutdown();
}
