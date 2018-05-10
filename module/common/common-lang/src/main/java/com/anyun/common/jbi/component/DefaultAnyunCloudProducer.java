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

package com.anyun.common.jbi.component;

import com.anyun.cloud.api.Response;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.exception.BaseCloudException;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/13/16
 */
public abstract class DefaultAnyunCloudProducer extends DefaultProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAnyunCloudProducer.class);
    private Component component;
    protected Exchange exchange;

    public DefaultAnyunCloudProducer(Component component, Endpoint endpoint) {
        super(endpoint);
        this.component = component;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        this.exchange = exchange;
        Object answer = producerProcess();
        LOGGER.debug("Answer object [{}]",JbiCommon.toJson(answer));
        long executedTime = System.currentTimeMillis() - currentTimeMillis;
        exchange.getOut().setHeader("anyuncloud_component_executed_time",executedTime);
        exchange.getOut().setBody(getDefaultAnswerFormatedPayload(answer),String.class);
    }

    private String getDefaultAnswerFormatedPayload(Object answer) {
        Response response = new Response();
        response.setContent(answer);
        response.setType(answer.getClass().getSimpleName());
        return JbiCommon.toJson(response);
    }

    /**
     * Anyun Cloud component producer process
     *
     * @return
     * @throws Exception
     */
    public abstract Object producerProcess() throws JbiComponentException;

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}