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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/7/16
 */
public class ComponentStartRoute extends RouteBuilder {
    public static final String URI_START = "anyun-msg://management:start";
    private  static final Logger LOGGER = LoggerFactory.getLogger(ComponentStartRoute.class);

    public ComponentStartRoute() {
        LOGGER.debug("Start message component ComponentStartRoute()");
    }

    @Override
    public void configure() throws Exception {
        LOGGER.debug("Start message component");
        from(URI_START).id("AMQPMessageComponentStartRoute").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
            }
        });
    }
}
