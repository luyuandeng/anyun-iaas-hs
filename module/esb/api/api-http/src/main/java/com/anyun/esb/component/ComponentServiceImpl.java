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

import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.api.jaxrs.DefaultApiProcessor;
import com.anyun.esb.component.api.jaxrs.JaxrsResource;
import com.anyun.esb.component.api.jaxrs.RestMethodDefine;
import org.apache.camel.component.jetty.JettyHttpComponent;
import org.apache.camel.component.jetty9.JettyHttpComponent9;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestConfigurationDefinition;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.util.jsse.KeyManagersParameters;
import org.apache.camel.util.jsse.KeyStoreParameters;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/11/16
 */
public class ComponentServiceImpl implements ComponentService {
    private Logger LOGGER = LoggerFactory.getLogger(ComponentServiceImpl.class);

    @Override
    public void initJettyComponent(Map<String, Object> configuration, ModelCamelContext context, RestConfigurationDefinition configurationDefinition) throws Exception {
        String schema = configuration.get("com.anyun.api.http.schema").toString();
        int port = Integer.valueOf(configuration.get("com.anyun.api.http.port").toString());
        String bindingModeString = configuration.get("com.anyun.api.http.binding_mode").toString();
        String baseUrl = configuration.get("com.anyun.api.http.url_base").toString();
        String listener = configuration.get("com.anyun.api.http.host").toString();
        LOGGER.info("schema [{}] port [{}] baseURL [" + baseUrl + "]", schema, port);
        LOGGER.info("listener host [{}]", listener);
        LOGGER.info("binding mode [{}]", bindingModeString);
        RestBindingMode bindingMode = RestBindingMode.valueOf(bindingModeString);

        initJettyContext(configuration, context);
        configurationDefinition.component("jetty");
        configurationDefinition.componentProperty("disableStreamCache","true");
        configurationDefinition.endpointProperty("disableStreamCache","true");
        configurationDefinition.scheme(schema).host(listener).port(port);
        configurationDefinition.bindingMode(bindingMode);
        LOGGER.debug("Jetty component initialzialed");
    }

    private void initJettyContext(Map<String, Object> configuration, ModelCamelContext context) throws Exception {
        JettyHttpComponent9 jettyComponent = context.getComponent("jetty", JettyHttpComponent9.class);
        String schema = configuration.get("com.anyun.api.http.schema").toString();
        if (schema.equals("https")) {
            LOGGER.info("Configuration Anyun Cloud HTTP Rest API SSL Context");
            String jksPath = configuration.get("com.anyun.api.http.ssl.jks.path").toString();
            String jksPasswd = configuration.get("com.anyun.api.http.ssl.jks.passwd").toString();
            KeyStoreParameters ksp = new KeyStoreParameters();
            ksp.setResource(jksPath);
            ksp.setPassword(jksPasswd);
            KeyManagersParameters kmp = new KeyManagersParameters();
            kmp.setKeyStore(ksp);
            kmp.setKeyPassword(jksPasswd);
            SSLContextParameters scp = new SSLContextParameters();
            scp.setKeyManagers(kmp);
            jettyComponent.setSslContextParameters(scp);
        }
    }

    private void initAnyunApiServices(ModelCamelContext context, RestDefinition rest) throws Exception {
        List<Class<?>> serviceClasses = JaxrsResource.getApiServices(context.getPackageScanClassResolver());
        for (Class<?> serviceClass : serviceClasses) {
            Path basePath = serviceClass.getAnnotation(Path.class);
            Method[] methods = serviceClass.getMethods();
            for (Method method : methods) {
                try {
                    Path path = method.getAnnotation(Path.class);
                    if (path == null)
                        continue;
                    Annotation httpMethod = JaxrsResource.getHttpMethodAnnotation(method);
                    Produces produces = method.getAnnotation(Produces.class);
                    Consumes consumes = method.getAnnotation(Consumes.class);
                    RestMethodDefine restMethodDefine = method.getAnnotation(RestMethodDefine.class);
                    if (restMethodDefine == null)
                        continue;
                    String url = basePath.value() + path.value();
                    LOGGER.debug("HTTP API Path [{}]", url);
                    if (httpMethod instanceof GET)
                        rest = rest.get(url);
                    else if (httpMethod instanceof PUT)
                        rest = rest.put(url);
                    else if (httpMethod instanceof DELETE)
                        rest = rest.delete(url);
                    else if (httpMethod instanceof POST)
                        rest = rest.post(url);
                    if (consumes != null) {
                        for (String consume : consumes.value()) {
                            rest.consumes(consume);
                            if (consume.contains("ocsp-request") || consume.contains("stream")) {
                                rest.bindingMode(RestBindingMode.off);
                            }
                            if(consume.contains("multipart/form-data")) {
                                rest.bindingMode(RestBindingMode.off);
                            }
                            LOGGER.debug("URL [{}] consume [{}]", url, consume);
                        }
                    }
                    String id = restMethodDefine.id();
                    if (id.equals("uuid"))
                        id = StringUtils.uuidGen();
                    LOGGER.debug("--------- " + id + "  --------" + restMethodDefine.service());
                    DefaultApiProcessor apiProcessor = new DefaultApiProcessor(restMethodDefine);
                    apiProcessor.setCamelContext(context);
                    apiProcessor.setProduces(produces);
                    apiProcessor.setConsumes(consumes);
                    rest.description(id, restMethodDefine.description(), restMethodDefine.lang())
                            .route().process(apiProcessor);
                } catch (Exception ex) {
                    continue;
                }
            }
        }
    }

    @Override
    public Map<String, Object> getConfigurations(ModelCamelContext context, String type) throws Exception {
        Jedis jedis = new Jedis("redis.dev.anyuncloud.com", 6379);
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("com.anyun.api.http.binding_mode", jedis.get("com.anyun.api.http.binding_mode"));
        configuration.put("com.anyun.api.http.host", jedis.get("com.anyun.api.http.host"));
        configuration.put("com.anyun.api.http.port", jedis.get("com.anyun.api.http.port"));
        configuration.put("com.anyun.api.http.schema", jedis.get("com.anyun.api.http.schema"));
        configuration.put("com.anyun.api.http.ssl.jks.passwd", jedis.get("com.anyun.api.http.ssl.jks.passwd"));
        configuration.put("com.anyun.api.http.ssl.jks.path", jedis.get("com.anyun.api.http.ssl.jks.path"));
        configuration.put("com.anyun.api.http.url_base", jedis.get("com.anyun.api.http.url_base"));
        return configuration;
    }

    @Override
    public void deployRests(Map<String, Object> configurations, ModelCamelContext context, RestDefinition rest) throws Exception {
        initAnyunApiServices(context, rest);
    }
}
