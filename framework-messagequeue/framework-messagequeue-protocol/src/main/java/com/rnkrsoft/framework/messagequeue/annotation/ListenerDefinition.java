package com.rnkrsoft.framework.messagequeue.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 * 用于标注在消息监听器上
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerDefinition {
    /**
     * 路由关键字
     *
     * @return 定义用于匹配的条件
     */
    Selector[] selectors();

    /**
     * 当发生错误重新放入队列
     * @return
     */
    boolean whenErrorRequeue() default false;

    /**
     * 如果whenErrorRequeue为真，则最大尝试处理年龄，超过则丢弃，未超过
     * @return
     */
    int maxTryProcessAge() default 3;

    /**
     * 是否使用ACK事务锁
     *
     * @return
     */
    boolean ack() default true;
}
