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

package com.anyun.common.jbi.component;

import com.anyun.cloud.tools.EncryptUtils;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.JbiCommon;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/13/16
 */
public class ComponentUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentUtil.class);

    public static Map<String, Object> getConfigurations(Endpoint baseEndpoint, String type) throws Exception {
        CamelContext camelContext = baseEndpoint.getCamelContext();
        Endpoint endpoint = camelContext.getEndpoint("anyun-da://db:configuration?operate=query&query=" + type);
        Exchange exchange = endpoint.createExchange();
        Producer producer = endpoint.createProducer();
        producer.process(exchange);
        String payload = exchange.getOut().getBody(String.class);
        LOGGER.info("get [{}] configuration from anyun cloud data access component [{}]", type, payload);
        Map<String, Object> configuration = JbiCommon.convertToAnyunEntity(Map.class, payload);
        camelContext.removeEndpoint(endpoint);
        return configuration;
    }

    public static String getLocalMessageComponentSerialNumber(Endpoint baseEndpoint) throws Exception {
        CamelContext camelContext = baseEndpoint.getCamelContext();
        Endpoint endpoint = camelContext.getEndpoint("anyun-msg://component:info?name=get_serialnumber");
        Exchange exchange = endpoint.createExchange();
        Producer producer = endpoint.createProducer();
        producer.process(exchange);
        String payload = exchange.getOut().getBody(String.class);
        LOGGER.debug("Get local message component serial number [{}]", payload);
        camelContext.removeEndpoint(endpoint);
        return payload;
    }

    public static String buildGetZnodeChildenURL(String zkString, String componentName) {
        StringBuilder sb = new StringBuilder();
        sb.append("zookeeper://");
        sb.append(zkString);
        if (StringUtils.isEmpty(componentName))
            sb.append("/anyuncloud/component");
        else
            sb.append("/anyuncloud/component/").append(componentName);
        sb.append("?listChildren=true");
        LOGGER.debug("buildGetZnodeChildenURL [{}]", sb.toString());
        return sb.toString();
    }

    public static String getSerialNumber(String componentName) {
        StringBuilder sb = new StringBuilder(componentName);
        Enumeration<NetworkInterface> nets = null;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return "serial_number_error";
        }
        for (NetworkInterface netint : Collections.list(nets)) {
            if (netint.getDisplayName().equals("lo") || netint.getName().equals("lo"))
                continue;
            LOGGER.debug("Display name: {}", netint.getDisplayName());
            sb.append(netint.getDisplayName());
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet6Address)
                    continue;
                sb.append(":").append(inetAddress.getHostAddress());
                LOGGER.debug("InetAddress: [{}] [{}]", inetAddress, inetAddress.getHostAddress());
            }
        }
        return EncryptUtils.getMD5ofStr(sb.toString()).toLowerCase();
    }

    public static String getMessageComponentSerialNumber() {
        String messageComponentName = "com.anyun.esb.component.message";
        return getSerialNumber(messageComponentName);
    }
}
