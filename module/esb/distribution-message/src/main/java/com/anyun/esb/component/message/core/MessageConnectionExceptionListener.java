package com.anyun.esb.component.message.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/12/16
 */
public class MessageConnectionExceptionListener implements ExceptionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConnectionExceptionListener.class);

    public MessageConnectionExceptionListener() {
    }

    @Override
    public void onException(JMSException exception) {
        LOGGER.error("AMQP connection error!",exception);
    }
}
