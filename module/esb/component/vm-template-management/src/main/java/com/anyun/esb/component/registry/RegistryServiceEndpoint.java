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

package com.anyun.esb.component.registry;

import com.anyun.cloud.tools.StringUtils;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/13/16
 */
@UriEndpoint(scheme = "anyun-registry",
        title = "Anyun Cloud Template Registry Service Endpoint",
        syntax = "anyun-registry:category:name",
        label = "anyun-registry",
        producerOnly = true)
public class RegistryServiceEndpoint extends DefaultEndpoint {

    @UriPath
    @Metadata(required = "true")
    private String category;

    @UriPath
    @Metadata(required = "true")
    private String name;

    @UriParam
    @Metadata(required = "false")
    private String option;

    private static String componentSerialNumber;
    @Override
    public Producer createProducer() throws Exception {
        if(category.equals("service")) {
            RegistryServiceProducer businessProducer = new RegistryServiceProducer(getComponent(),this);
            businessProducer.setComponentName(name);
            businessProducer.setServiceName(option);
            return businessProducer;
        } else if(category.equals("component")) {
            ComponentProducer componentProducer = new ComponentProducer(this);
            componentProducer.setOption(name);
        }
        throw new UnsupportedOperationException("Unsupported component category [" + category + "]");
    }

    public RegistryServiceEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
        if(componentSerialNumber == null)
            componentSerialNumber = StringUtils.uuidGen();
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        if(category.equals("component")) {
            if(name.equals("start")) {

                ComponentStartConsumer consumer = new ComponentStartConsumer(getComponent(),this,processor);
                if(StringUtils.isNotEmpty(option))
                    consumer.setOption(option);
                return consumer;
            }
            throw new UnsupportedOperationException("Unsupported component operate type [" + name + "]");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getComponentSerialNumber() {
        return componentSerialNumber;
    }
}
