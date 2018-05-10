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

import com.anyun.esb.component.message.core.amqp.AMQPTopicMessageConsumer;
import com.anyun.esb.component.message.core.ComponentRegistConsumer;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/7/16
 */
public class ComponentStartConsumer extends DefaultConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentStartConsumer.class);
    private Component component;
    private AMQPTopicMessageConsumer amqpMessageConsumer;
    private ComponentRegistConsumer componentRegistConsumer;
    private int timeout = 0;

    public ComponentStartConsumer(Component component, Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.component = component;
        amqpMessageConsumer = new AMQPTopicMessageConsumer(component, endpoint, processor);
        componentRegistConsumer = new ComponentRegistConsumer(component, endpoint, processor);
    }

    @Override
    protected void doStart() throws Exception {
        if (timeout != 0) {
            amqpMessageConsumer.setTimeout(timeout);
            LOGGER.info("Set AMQP message consumer timeout to [{}]", timeout);
        }
        amqpMessageConsumer.start();
        componentRegistConsumer.start();
    }

    @Override
    protected void doShutdown() throws Exception {
        super.doShutdown();
        amqpMessageConsumer.stop();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        amqpMessageConsumer.stop();
        componentRegistConsumer.stop();
        ComponentEnv.getEnv().getJedis().close();
        ComponentEnv.getEnv().getQueryConnector().close();
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
