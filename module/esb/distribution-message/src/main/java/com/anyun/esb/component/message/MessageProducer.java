/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.esb.component.message;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.message.core.amqp.AmqFactory;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.qpid.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/8/16
 */
public class MessageProducer extends DefaultProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);
    private Component component;
    private String category;
    private String name;

    public MessageProducer(Component component, Endpoint endpoint) {
        super(endpoint);
        this.component = component;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if (category.equals("topic"))
            processTopic(exchange);
        else
            throw new UnsupportedOperationException("Unsupported message type [" + category + "]");
    }

    public void processTopic(Exchange exchange) throws Exception {
        Session session = AmqFactory.getFactory().getRandomSession();
        JmsTopic topic = new JmsTopic(AmqFactory.QUEUE_ANYUN_MSG);
        javax.jms.MessageProducer messageProducer = session.createProducer(topic);
        String payload = exchange.getIn().getBody(String.class);
        String jmsCorrelationId = "anyun-amqp-" + StringUtils.uuidGen();
        if (StringUtils.isEmpty(payload))
            payload = "";
        TextMessage message = session.createTextMessage(payload);
        message.setJMSCorrelationID(jmsCorrelationId);
        Map<String, Object> headers = exchange.getIn().getHeaders();
        message.setJMSReplyTo(topic);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                message.setObjectProperty(entry.getKey(), entry.getValue());
            }
        }
        LOGGER.debug("Set AnyunCloud component name [{}]",name);
        message.setStringProperty("anyuncloud_component_name",name);
        messageProducer.send(message, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
        LOGGER.debug("Anyun Cloud AMQP message [{}] sended", message);
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
