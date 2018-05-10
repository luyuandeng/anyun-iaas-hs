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

package com.anyun.esb.component;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/7/16
 */
@UriEndpoint(scheme = "anyun-rest",
        title = "Anyun Cloud Rest Api Management Endpoint",
        syntax = "anyun-api:category:type",
        label = "api-management",
        producerOnly = true)
public class RestApiManagementEndpoint extends DefaultEndpoint{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRestApiComponent.class);
    @UriPath(enums = "component,api")
    @Metadata(required = "true")
    private String category = "";

    @UriPath(enums = "management")
    @Metadata(required = "true")
    private String type = "";

    @UriParam()
    private String operate = "";

    public RestApiManagementEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("Unsupported component category [" + category + "]");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        if(category.equals("component")) {
            if(operate.equals("start"))
                return new ComponentStartConsumer(getComponent(),this,processor);
            throw new UnsupportedOperationException("Unsupported component operate [" + operate + "]");
        }
        throw new UnsupportedOperationException("Unsupported component category [" + category + "]");
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
