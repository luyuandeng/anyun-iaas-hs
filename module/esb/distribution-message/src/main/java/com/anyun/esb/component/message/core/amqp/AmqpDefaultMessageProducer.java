package com.anyun.esb.component.message.core.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/15/16
 */
public class AmqpDefaultMessageProducer implements AmqpMessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpDefaultMessageProducer.class);
    private int timeout = 100;
    private Message payload;
    private String messageId;

    private CountDownLatch connectedSignal = new CountDownLatch(1);

    @Override
    public void restSignal() throws Exception {
        if (connectedSignal == null)
            throw new Exception("Message [" + messageId + "] process timeout");
        if (connectedSignal.getCount() > 0)
            connectedSignal.countDown();
    }

    @Override
    public final Message process(String messageId) throws Exception {
        this.messageId = messageId;
        LOGGER.debug("Message is send,wating for response,timeout [{}]",timeout);
        connectedSignal.await(timeout, TimeUnit.SECONDS);
        if (connectedSignal.getCount() != 0) {
            connectedSignal = null;
            LOGGER.warn("Message [{}] process timeout",messageId);
            throw new Exception("Message [" + messageId + "] process timeout");
        }
        return payload;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public void setPayload(Message payload) {
        this.payload = payload;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
