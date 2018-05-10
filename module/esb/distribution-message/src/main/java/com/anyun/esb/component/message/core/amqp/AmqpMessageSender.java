package com.anyun.esb.component.message.core.amqp;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.message.ComponentEnv;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Exchange;
import org.apache.qpid.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/16/16
 */
public class AmqpMessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmqpMessageSender.class);
    private Exchange exchange;
    private Message message;
    private String messageId;
    public AmqpMessageSender(Exchange exchange) {
        this.exchange = exchange;
    }

    public AmqpMessageSender build() throws Exception {
        Object payload = exchange.getIn().getBody();
        Session session = AmqFactory.getFactory().releaseSession();
        Object consumesHeader = exchange.getIn().getHeader("anyuncloud_request_consumes");
        LOGGER.debug("Request consumes header [{}]",consumesHeader);
        if(consumesHeader != null) {
            String [] consumes = StringUtils.getSplitValues((String) consumesHeader,",");
            for (String consume : consumes) {
                if(consume.contains("ocsp-request") || consume.contains("multipart/form-data")) {
                    payload = exchange.getIn().getBody(byte[].class);
                    message = session.createBytesMessage();
                    ((BytesMessage) message).writeBytes((byte[]) payload);
                    LOGGER.debug("request is ocsp request message");
                    break;
                } else {
                    message = session.createTextMessage();
                    ((TextMessage) message).setText(String.valueOf(payload));
                    LOGGER.debug("Request is text message");
                    break;
                }
            }
        } else {
            message = session.createTextMessage();
            ((TextMessage) message).setText(String.valueOf(payload));
            LOGGER.debug("Request is text message");
        }
        messageId = "anyuncloud-" + StringUtils.uuidGen();
        message.setJMSCorrelationID(messageId);
        message.setStringProperty("anyuncloud_message_id", messageId);
        Map<String, Object> exchangeHeaders = exchange.getIn().getHeaders();
        if (exchangeHeaders != null && !exchangeHeaders.isEmpty()) {
            for (Map.Entry<String, Object> entry : exchangeHeaders.entrySet()) {
                if (!(entry.getValue() instanceof String))
                    continue;
                String key = entry.getKey().replace(".", "_").replace("-", "_");
                message.setObjectProperty(key, entry.getValue());
            }
        }
        message.setObjectProperty("anyuncloud_message_type", "request");
        message.setObjectProperty("anyuncloud_message_build_time", System.currentTimeMillis());
        return this;
    }

    public void receive() throws JbiComponentException {
        AmqpDefaultMessageProducer producer = new AmqpDefaultMessageProducer();
        producer.setMessageId(messageId);
        ComponentEnv.getEnv().getMessageProducers().add(producer);
        LOGGER.debug("Add process to list [{}]", producer.getMessageId());
        Session session = ComponentEnv.getEnv().getAmqpConnectSession();
        MessageProducer messageProducer = null;
        Message payload = null;
        String responseMessageType = null;
        String responseExceptionType = null;
        String text = null;
        try {
            messageProducer = session.createProducer(new JmsTopic(AmqFactory.QUEUE_ANYUN_MSG));
            messageProducer.send(message, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
            payload = producer.process(messageId);
            LOGGER.debug("Producer process size [{}]", ComponentEnv.getEnv().getMessageProducers().size());
            responseMessageType = payload.getStringProperty("anyuncloud_message_response_type");
            responseExceptionType = payload.getStringProperty("anyuncloud_exception_type");
            if (!(payload instanceof TextMessage))
                throw new UnsupportedOperationException("Unsupported message type [" + payload.getClass().getName() + "]");
            text = ((TextMessage) payload).getText();
            if (StringUtils.isEmpty(responseMessageType))
                throw new Exception("Message not have property [anyuncloud_message_response_type]");
        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(1000, 1002);
            exception.setMessage("Message system error [" + ex.getMessage() + "]");
            exception.setUserMessage("消息系统异常 [" + exception.getMessage() + "]");
            exception.setUserTitle("消息系统异常");
            throw exception;
        }

        if (responseMessageType.equals("message_error")) {
            LOGGER.debug("Message error type [{}]", responseExceptionType);
            if (responseExceptionType.equals(JbiComponentException.class.getSimpleName())) {
                LOGGER.debug("Message component exception message [{}]", text);
                JbiExceptionEntity entity = JsonUtil.fromJson(JbiExceptionEntity.class, text);
                JbiComponentException exception = new JbiComponentException(entity.getCode(), entity.getSubCode());
                exception.setMessage(entity.getMessage());
                exception.setUserMessage(entity.getUserMessage());
                exception.setUserTitle(entity.getUserTitle());
                throw exception;
            } else {
                JbiComponentException exception = new JbiComponentException(1000, 1005);
                exception.setMessage(text);
                exception.setUserMessage("系统异常[" + text + "]");
                exception.setUserTitle("系统异常");
                throw exception;
            }
        } else if (responseMessageType.equals("success")) {
            try {
                if (StringUtils.isNotEmpty(text))
                    exchange.getOut().setBody(text);
                List<String> propNames = Collections.list(payload.getPropertyNames());
                if (propNames != null && !propNames.isEmpty()) {
                    for (String propName : propNames) {
                        exchange.getOut().setHeader(propName, payload.getObjectProperty(propName));
                    }
                }
            } catch (Exception ex) {
                JbiComponentException exception = new JbiComponentException(1000, 1002);
                exception.setMessage("Message system error [" + ex.getMessage() + "]");
                exception.setUserMessage("消息系统异常 [" + exception.getMessage() + "]");
                exception.setUserTitle("消息系统异常");
                throw exception;
            }
        }
    }
}
