package com.anyun.esb.component.message.core.amqp;

import com.anyun.esb.component.message.ComponentEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/15/16
 */
public class MessageReceiveProcess implements Runnable {
    private Thread thread;
    private static Logger LOGGER = LoggerFactory.getLogger(MessageReceiveProcess.class);
    private AmqpMessageProducer producer;
    private String producerId;

    public MessageReceiveProcess(AmqpMessageProducer producer) {
        this.producer = producer;
        producerId = producer.getMessageId();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        ComponentEnv.getEnv().removeMessageProducer(producerId);
        try {
            producer.restSignal();
        } catch (Exception ex) {
            LOGGER.warn("Producer [{}] timeout,remove it",producerId);
        }
    }
}
