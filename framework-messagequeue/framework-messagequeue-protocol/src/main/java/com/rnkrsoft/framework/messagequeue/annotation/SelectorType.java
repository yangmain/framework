package com.rnkrsoft.framework.messagequeue.annotation;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public enum SelectorType {
    /**
     * 任何条件下都执行
     */
    necessarily,
    /**
     * 熔断，只要满足条件就结束调用监听器
     */
    fusing,
}
