package com.rnkrsoft.framework.messagequeue.protocol;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
public class DemoProducer implements MessageQueueProducer{

    @Override
    public void setUri(String uri) {

    }

    @Override
    public void setUsername(String username) {

    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

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
