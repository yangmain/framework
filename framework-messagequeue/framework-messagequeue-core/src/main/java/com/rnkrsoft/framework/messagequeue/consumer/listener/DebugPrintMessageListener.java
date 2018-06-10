package com.rnkrsoft.framework.messagequeue.consumer.listener;

import com.rnkrsoft.time.DateStyle;
import com.rnkrsoft.utils.DateUtils;
import com.rnkrsoft.framework.messagequeue.annotation.ListenerDefinition;
import com.rnkrsoft.framework.messagequeue.annotation.Selector;
import com.rnkrsoft.framework.messagequeue.annotation.SelectorType;
import com.rnkrsoft.framework.messagequeue.protocol.Message;
import com.rnkrsoft.framework.messagequeue.protocol.MessageQueueListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/5/21.
 */
@ListenerDefinition(
        selectors = {
                //任何信息都能匹配
                @Selector(type = SelectorType.necessarily, routingKey = "*")
        },
        ack = true,
        whenErrorRequeue = true,
        maxTryProcessAge = 1
)
@Slf4j
public class DebugPrintMessageListener implements MessageQueueListener {
    @Override
    public void execute(Message message) {
        if (log.isDebugEnabled()) {
            String str = message.asJson();
            log.info(
                    "message '{}' age '{}', create date '{}', last update date '{}'",
                    str,
                    message.getAge(),
                    DateUtils.formatJavaDate2String(new Date(message.getCreateDate()), DateStyle.FILE_FORMAT2),
                    DateUtils.formatJavaDate2String(new Date(message.getLastUpdateDate()), DateStyle.FILE_FORMAT2)
            );
        }
    }
}
