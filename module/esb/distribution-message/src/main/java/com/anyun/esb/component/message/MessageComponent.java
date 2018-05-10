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

import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/8/16
 */
public class MessageComponent extends UriEndpointComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageComponent.class);

    public MessageComponent() {
        super(MessageEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        MessageEndpoint endpoint = new MessageEndpoint(uri, this);
        String category = ObjectHelper.before(remaining,":");
        String verb = ObjectHelper.after(remaining,":");
        endpoint.setCategory(category);
        endpoint.setVerb(verb);
        setProperties(endpoint, parameters);
        LOGGER.debug("Endpoint [{}] created", MessageEndpoint.class.getName());
        return endpoint;
    }
}
