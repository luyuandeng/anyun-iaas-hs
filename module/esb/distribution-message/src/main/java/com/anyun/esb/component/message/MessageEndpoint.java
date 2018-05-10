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
 * @date 4/8/16
 */
@UriEndpoint(scheme = "anyun-msg",
        title = "Anyun Message Endpoint",
        syntax = "anyun-msg:category:verb",
        label = "message-queue")
public class MessageEndpoint extends DefaultEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEndpoint.class);
    @UriPath()
    @Metadata(required = "true")
    private String category;
    @UriPath()
    @Metadata(required = "true")
    private String verb;
    @UriParam
    @Metadata
    private String name;
    private ComponentStartConsumer componentStartConsumer;

    public MessageEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        if(category.equals("service")) {
            if(verb.equals("component")) {
                ComponentServiceProducer componentServiceProducer = new ComponentServiceProducer(this);
                componentServiceProducer.setComponentName(name);
                return componentServiceProducer;
            }
            throw new UnsupportedOperationException("Unsupported component verb [" + verb + "]");
        } else if(category.equals("component")) {
            if(verb.equals("info")) {
                ComponentProducer componentProducer = new ComponentProducer(this);
                componentProducer.setOption(name);
                return componentProducer;
            }

        }
        throw new UnsupportedOperationException("Unsupported component category [" + category + "]");
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        if (category.equals("management")) {
            if (verb.equals("start")) {
                componentStartConsumer = new ComponentStartConsumer(getComponent(), this, processor);
                return componentStartConsumer;
            }
            throw new UnsupportedOperationException("Unsupported component verb [" + verb + "]");
        }
        throw new UnsupportedOperationException("Unsupported component category [" + category + "]");
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
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
