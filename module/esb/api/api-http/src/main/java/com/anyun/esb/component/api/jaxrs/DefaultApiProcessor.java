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

package com.anyun.esb.component.api.jaxrs;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.tools.FileUtil;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.esb.component.api.jaxrs.request.MultiPartFileHandler;
import com.anyun.esb.component.api.jaxrs.response.JsonResponseBuilder;
import com.anyun.esb.component.api.jaxrs.response.OcspResponseBuilder;
import com.anyun.esb.component.api.jaxrs.response.ResponseBuilder;
import com.anyun.esb.component.api.jaxrs.response.StreamBase64ResponseBuilder;
import com.anyun.exception.AnyunCloudAuthenticationException;
import com.anyun.exception.BaseCloudException;
import com.anyun.exception.JbiComponentException;
import com.anyun.exception.ServerErrorEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.*;
import org.apache.camel.http.common.HttpMessage;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/19/16
 */
public class DefaultApiProcessor implements Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultApiProcessor.class);
    private static final String PATH_UPLOAD_STORAGE = "/tmp";
    private CamelContext camelContext;
    private RestMethodDefine restMethodDefine;
    private Produces produces;
    private Consumes consumes;
    private MultiPartFileHandler multiPartFileHandler;
    private static List<ResponseBuilder> responseBuilderList = new ArrayList<>();

    static {
        responseBuilderList.add(new JsonResponseBuilder());
        responseBuilderList.add(new OcspResponseBuilder());
        responseBuilderList.add(new StreamBase64ResponseBuilder());
    }

    public DefaultApiProcessor(RestMethodDefine restMethodDefine) throws Exception {
        this.restMethodDefine = restMethodDefine;
        if (!restMethodDefine.uploadHandlerClass().getName().equals(MultiPartFileHandler.class.getName()))
            multiPartFileHandler = restMethodDefine.uploadHandlerClass().newInstance();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String, Object> headers = exchange.getIn().getHeaders();
        Object multPartObj = null;
        try {
//            if (restMethodDefine.needAuthentication()) {
//                Object anyuncloudToken = headers.get("anyun_cloud_user_token");
//                authentication(anyuncloudToken);
//            }
            if (consumes != null) {
                StringBuilder sb = new StringBuilder();
                for (String consume : consumes.value()) {
                    sb.append(",").append(consume);
                    if (consume.contains("ocsp-request") || consume.contains("stream")) {
                        InputStream streamCache = (InputStream) exchange.getIn().getBody();
                        LOGGER.debug("{}", exchange.getIn().getHeaders());
                        DataInputStream dataInputStream = new DataInputStream(streamCache);
                        LOGGER.debug("Request content length [{}]", dataInputStream.available());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int r = dataInputStream.read(buffer);
                            if (r == -1) break;
                            byteArrayOutputStream.write(buffer, 0, r);
                        }
                        byteArrayOutputStream.flush();
                        byte[] ocspReqBytes = byteArrayOutputStream.toByteArray();
                        exchange.getIn().setBody(ocspReqBytes, byte[].class);
                        LOGGER.debug("Build exchange body to bytes from ocsp request");
                    } else if (consume.contains("multipart/form-data")) {
                        multPartObj = processMultPart(exchange);
                        if(!multiPartFileHandler.isChain()) {
                            String response = getDefaultAnswerFormatedPayload(multPartObj);
                            ObjectMapper mapper = new ObjectMapper();
                            Response answerObj = mapper.readValue(response, Response.class);
                            for (ResponseBuilder responseBuilder : responseBuilderList) {
                                if (responseBuilder.match(produces)) {
                                    LOGGER.debug("Response builder [{}] ready", responseBuilder.getClass().getName());
                                    Object responseBody = responseBuilder.build(restMethodDefine, startTime, answerObj);
                                    exchange.getOut().setBody(responseBody, responseBuilder.getResponseType());
                                    return;
                                }
                            }
                        } else {
                            exchange.getIn().setBody(null);
                        }
                    }
                }
                if (StringUtils.isNotEmpty(sb.toString())) {
                    exchange.getIn().setHeader("anyuncloud_request_consumes", sb.toString().substring(1));
                }
            }
            exchange.getIn().setHeader("business_component", restMethodDefine.component());
            exchange.getIn().setHeader("business_service", restMethodDefine.service());
            LOGGER.debug("Reques headers[{}]", exchange.getIn().getHeaders());
            LOGGER.debug("Request body type [{}]", exchange.getIn().getBody().getClass().getName());
            LOGGER.debug("Reques body[{}]", exchange.getIn().getBody());
            Producer serviceProducer = null;
            try {
                Endpoint serviceEndpoint = camelContext.getEndpoint("anyun-msg://service:component?name=" + restMethodDefine.component());
                serviceProducer = serviceEndpoint.createProducer();
            } catch (Exception ex) {
                throw new Exception("Message component not available");
            }
            serviceProducer.process(exchange);
            String response = exchange.getOut().getBody(String.class);
            ObjectMapper mapper = new ObjectMapper();
            Response answerObj = mapper.readValue(response, Response.class);
            for (ResponseBuilder responseBuilder : responseBuilderList) {
                if (responseBuilder.match(produces)) {
                    LOGGER.debug("Response builder [{}] ready", responseBuilder.getClass().getName());
                    Object responseBody = responseBuilder.build(restMethodDefine, startTime, answerObj);
                    exchange.getOut().setBody(responseBody, responseBuilder.getResponseType());
                    return;
                }
            }
        } catch (JbiComponentException ex) {
            exchange.getOut().setBody(buildErrorJson(ex), String.class);
        } catch (AnyunCloudAuthenticationException ex) {
            exchange.getOut().setBody(buildErrorJson(ex), String.class);
        }

    }

    private String getDefaultAnswerFormatedPayload(Object answer) {
        Response response = new Response();
        response.setContent(answer);
        response.setType(answer.getClass().getSimpleName());
        return JbiCommon.toJson(response);
    }

    public Object processMultPart(Exchange exchange) throws Exception {
        HttpMessage httpMessage = (HttpMessage) exchange.getIn();
        HttpServletRequest request = httpMessage.getRequest();
        String location = PATH_UPLOAD_STORAGE;  // the directory location where files will be stored
        long maxFileSize = 100000000;  // the maximum size allowed for uploaded files
        long maxRequestSize = 100000000;  // the maximum size allowed for multipart/form-data requests
        int fileSizeThreshold = 1024;  // the size threshold after which files will be written to disk
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
        request.setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        List<MultiPartInputStreamParser.MultiPart> multiParts = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part instanceof MultiPartInputStreamParser.MultiPart) {
                MultiPartInputStreamParser.MultiPart multiPart = (MultiPartInputStreamParser.MultiPart) part;
                if (multiPart.getFile() != null)
                    multiParts.add(multiPart);
            }
        }
        Object object = multiPartFileHandler.process(multiParts, request, exchange);
        for (MultiPartInputStreamParser.MultiPart multiPart : multiParts) {
            FileUtil.rm(multiPart.getFile().getPath(), true);
        }
        return object;
    }

    private void authentication(Object anyuncloudToken) throws AnyunCloudAuthenticationException {
        try {
            if (anyuncloudToken == null || StringUtils.isEmpty((String) anyuncloudToken))
                throw new AnyunCloudAuthenticationException(new Exception("User token not set"));
            Endpoint serviceEndpoint = camelContext.getEndpoint("anyun-msg://service:component?name=anyun-business");
            Producer serviceProducer = serviceEndpoint.createProducer();
            Exchange exchange = serviceEndpoint.createExchange();
            exchange.getIn().setHeader("business_component", "anyun-business");
            exchange.getIn().setHeader("business_category", "user_token_authentication");
            exchange.getIn().setHeader("business_service", "user_token_authentication");
            exchange.getIn().setBody(anyuncloudToken);
            serviceProducer.process(exchange);
            LOGGER.debug("Authentication message headers [{}]", exchange.getOut().getHeaders());
            LOGGER.debug("Authentication message body [\n{}\n]", exchange.getOut().getBody(String.class));
        } catch (Exception exception) {
            LOGGER.warn("AnyunCloud authentication service failed [{}]", exception.getMessage());
            AnyunCloudAuthenticationException authenticationException = new AnyunCloudAuthenticationException(exception);
            authenticationException.setMessage("AnyunCloud authentication service failed [" + exception.getMessage() + "]");
            authenticationException.setUserMessage("用户认证错误[" + exception.getMessage() + "]");
            authenticationException.setUserTitle("authentication failed");
            throw authenticationException;
        }
    }

    private String buildErrorJson(BaseCloudException ex) {
        LOGGER.debug("Exception message [{}]", ex.getMessage());
        ServerErrorEntity.ServerError error = new ServerErrorEntity.ServerError();
        error.setCode(ex.getCode());
        error.setError_subcode(ex.getSubCode());
        error.setError_user_msg(ex.getUserMessage());
        error.setError_user_title(ex.getUserTitle());
        error.setMessage(ex.getMessage());
        error.setType(ex.getType());
        ServerErrorEntity errorEntity = new ServerErrorEntity();
        errorEntity.setError(error);
        return JbiCommon.toJson(errorEntity);
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public void setConsumes(Consumes consumes) {
        this.consumes = consumes;
    }

    public void setProduces(Produces produces) {
        this.produces = produces;
    }
}
