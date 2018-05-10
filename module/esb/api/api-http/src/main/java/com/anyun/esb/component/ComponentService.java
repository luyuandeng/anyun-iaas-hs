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

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.rest.RestConfigurationDefinition;
import org.apache.camel.model.rest.RestDefinition;

import java.util.Map;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/7/16
 */
public interface ComponentService {

    /**
     *
     * @param configuration
     * @param context
     * @param configurationDefinition
     * @throws Exception
     */
    void initJettyComponent(Map<String, Object> configuration, ModelCamelContext context, RestConfigurationDefinition configurationDefinition) throws Exception;

    /**
     *
     * @param context
     * @param type
     * @return
     * @throws Exception
     */
    Map<String, Object> getConfigurations(ModelCamelContext context, String type) throws Exception;

    /**
     *
     * @param configurations
     * @param context
     * @param rest
     * @throws Exception
     */
    void deployRests(Map<String, Object> configurations, ModelCamelContext context, RestDefinition rest) throws Exception;
}
