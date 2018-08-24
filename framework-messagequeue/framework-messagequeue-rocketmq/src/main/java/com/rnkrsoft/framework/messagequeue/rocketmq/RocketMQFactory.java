package com.rnkrsoft.framework.messagequeue.rocketmq;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import org.apache.rocketmq.client.consumer.MQConsumer;

import java.util.Properties;

/**
 * Created by rnkrsoft.com on 2018/7/16.
 */
public class RocketMQFactory {
    /**
     * 创建Consumer
     * <p>
     *     <code>properties</code>应该至少包含以下几项配置内容:
     *     <ol>
     *         <li>{@link PropertyKeyConst#ConsumerId}</li>
     *         <li>{@link PropertyKeyConst#AccessKey}</li>
     *         <li>{@link PropertyKeyConst#SecretKey}</li>
     *         <li>{@link PropertyKeyConst#ONSAddr}</li>
     *     </ol>
     *     以下为可选配置项:
     *     <ul>
     *         <li>{@link PropertyKeyConst#ConsumeThreadNums}</li>
     *         <li>{@link PropertyKeyConst#ConsumeTimeout}</li>
     *         <li>{@link PropertyKeyConst#OnsChannel}</li>
     *     </ul>
     * </p>
     * @param properties Consumer的配置参数
     * @return {@code Consumer} 实例
     */
    public static MQConsumer createConsumer(final Properties properties) {
        return null;
    }
}
