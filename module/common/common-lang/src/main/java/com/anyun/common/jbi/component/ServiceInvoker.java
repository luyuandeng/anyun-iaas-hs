package com.anyun.common.jbi.component;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.tools.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class ServiceInvoker<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInvoker.class);
    private static final ThreadLocal<CamelContext> CAMEL_CONTEXT = new ThreadLocal<>();
    private String component;
    private String service;

    public T invoke(Map<String, String> params, String body) throws Exception {
        String response = invoke1(params,body);
        ObjectMapper mapper = new ObjectMapper();
        Response<T> answerObj = mapper.readValue(response, Response.class);
        return answerObj.getContent();
    }

    public String invoke1(Map<String, String> params, String body) throws Exception {
        LOGGER.debug("Camel context [{}]", CAMEL_CONTEXT.get());
        Producer serviceProducer = null;
        Endpoint serviceEndpoint = CAMEL_CONTEXT.get().getEndpoint("anyun-msg://service:component?name=" + component);
        serviceProducer = serviceEndpoint.createProducer();
        Exchange exchange = new DefaultExchange(serviceEndpoint);
        exchange.getIn().setHeader("business_component", component);
        exchange.getIn().setHeader("business_service", service);
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                exchange.getIn().setHeader(param.getKey(), param.getValue());
            }
        }
        if (StringUtils.isNotEmpty(body))
            exchange.getIn().setBody(body, String.class);
        serviceProducer.process(exchange);
        String response = exchange.getOut().getBody(String.class);
       return response;
    }

    public static CamelContext getCamelContext() {
        return CAMEL_CONTEXT.get();
    }

    public static void setCamelContext(CamelContext camelContext) {
        CAMEL_CONTEXT.set(camelContext);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
