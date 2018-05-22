package com.rnkrsoft.framework.messagequeue.annotation;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public @interface Selector {
    /**
     * 操作类型，默认熔断
     * @return
     */
    SelectorType type() default SelectorType.fusing;

    /**
     * 路由关键字
     * @return
     */
    String routingKey();
}
