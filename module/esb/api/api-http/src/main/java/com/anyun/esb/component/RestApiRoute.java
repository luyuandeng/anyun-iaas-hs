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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/7/16
 */
public class RestApiRoute extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiRoute.class);
    private static final String URI_START = "anyun-rest://component:management?operate=start";
    private static final String CATEGORY_CONFIG_REST = "com.anyun.api.http";

    private ComponentService restApiService;

    public RestApiRoute() {
        restApiService = new ComponentServiceImpl();
    }

    @Override
    public void configure() throws Exception {
        from(URI_START).id("RestApiComponentStartRoute").process(new BlankProcessor());
        Map<String, Object> configuration = restApiService.getConfigurations(getContext(), CATEGORY_CONFIG_REST);
        restApiService.initJettyComponent(configuration, getContext(), restConfiguration());
        String baseUrl = configuration.get("com.anyun.api.http.url_base").toString();
        LOGGER.info("Anyun Cloud Base URL [{}]", baseUrl);
        RestDefinition rest = rest(baseUrl);
        restApiService.deployRests(configuration, getContext(), rest);
    }
}
