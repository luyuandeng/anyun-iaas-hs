package com.anyun.esb.component.message.core.amqp;

import org.apache.camel.Exchange;

import javax.jms.Message;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/15/16
 */
public interface AmqpMessageProducer {
    void restSignal() throws Exception;

    Message process(String messageId) throws Exception;

    void setPayload(Message payload);

    int getTimeout();

    String getMessageId();
}
