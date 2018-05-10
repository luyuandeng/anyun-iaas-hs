package com.anyun.esb.component.message.core.service;

import com.anyun.esb.component.message.ComponentEnv;
import com.anyun.esb.component.message.core.amqp.AmqFactory;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
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
public class ServiceMessageRequestProcess {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMessageRequestProcess.class);

    public void process(Endpoint endpoint, String messageId, Message message) throws Exception {
        String componentName = message.getStringProperty("business_component");
        String serviceName = message.getStringProperty("business_service");
        String endpointURL = componentName + "://service:" + componentName + "?option=" + serviceName;
        LOGGER.debug("Process service endpoint url [{}]", endpointURL);
        Endpoint serviceComponentEndpoint = endpoint.getCamelContext().getEndpoint(endpointURL);
        Producer serviceProducer = serviceComponentEndpoint.createProducer();
        Exchange exchange = serviceComponentEndpoint.createExchange();
        exchange.getIn().setBody("");
        exchange.getIn().setHeader("anyuncloud_message_entry", message);
        exchange.getIn().setHeader("anyuncloud_message_id", messageId);
        List<String> headerNames = Collections.list(message.getPropertyNames());
        for (String headerName : headerNames) {
            exchange.getIn().setHeader(headerName, message.getObjectProperty(headerName));
        }
        LOGGER.debug("Message type [{}]", message.getClass().getName());
        if (message instanceof TextMessage) {
            String messageText = ((TextMessage) message).getText();
            exchange.getIn().setBody(messageText, String.class);
            LOGGER.debug("Process text message service");
        } else if (message instanceof BytesMessage) {
            BytesMessage bytesMessage = (BytesMessage) message;
            LOGGER.debug("bytes length [{}]", bytesMessage.getBodyLength());
            byte[] messageBytes = new byte[Long.valueOf(bytesMessage.getBodyLength()).intValue()];
            bytesMessage.readBytes(messageBytes);
            exchange.getIn().setBody(messageBytes, byte[].class);
            LOGGER.debug("Process bytes message service");
        }
        serviceProducer.process(exchange);
        sendExchange(messageId, exchange);
    }

    private void sendExchange(String messageId, Exchange exchange) throws Exception {
        LOGGER.debug("Service processed header [{}]", exchange.getOut().getHeaders());
        Session session = ComponentEnv.getEnv().getAmqpConnectSession();
        TextMessage successMessage = session.createTextMessage();
        successMessage.setStringProperty("anyuncloud_message_id", messageId);
        successMessage.setStringProperty("anyuncloud_message_type", "response");
        successMessage.setStringProperty("anyuncloud_message_response_type", "success");
        successMessage.setText(exchange.getOut().getBody(String.class));
        Map<String, Object> headers = exchange.getOut().getHeaders();
        for (Map.Entry<String, Object> header : headers.entrySet()) {
            successMessage.setObjectProperty(header.getKey(), header.getValue());
        }
        MessageProducer errorMessageProducer = session.createProducer(new JmsTopic(AmqFactory.QUEUE_ANYUN_MSG));
        errorMessageProducer.send(
                successMessage,
                DeliveryMode.NON_PERSISTENT,
                Message.DEFAULT_PRIORITY,
                Message.DEFAULT_TIME_TO_LIVE);
    }
}
