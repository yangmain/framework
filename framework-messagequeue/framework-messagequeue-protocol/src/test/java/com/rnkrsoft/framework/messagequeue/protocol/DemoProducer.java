package com.rnkrsoft.framework.messagequeue.protocol;

/**
 * Created by woate on 2018/5/21.
 */
public class DemoProducer implements MessageQueueProducer{
    @Override
    public int produce(Message message) {
        return SUCCESS;
    }

    @Override
    public int produce(Object bean) {
        Message message = new Message("");
        return SUCCESS;
    }
}
