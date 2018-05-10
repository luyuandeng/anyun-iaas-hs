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

package com.anyun.esb.component.storage;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.BusinessService;
import com.anyun.common.jbi.component.DefaultAnyunCloudProducer;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.storage.service.ServiceResource;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/13/16
 */
public class StorageServiceProducer extends DefaultAnyunCloudProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceProducer.class);
    private String componentName;
    private String serviceName;

    public StorageServiceProducer(Component component, Endpoint endpoint) {
        super(component, endpoint);
    }

    @Override
    public Object producerProcess() throws JbiComponentException {
        LOGGER.debug("Local service component name [{}]",componentName);
        LOGGER.debug("Local Service name [{}]",serviceName);
        LOGGER.debug("Headers [{}]",exchange.getIn().getHeaders());
        if(StringUtils.isEmpty(componentName)) {
            JbiComponentException exception = new JbiComponentException(1000,1004);
            exception.setMessage("Component Name is not set");
            exception.setUserMessage("组件名称没有设置");
            exception.setUserTitle("组件调用失败");
            throw exception;
        }
        if(StringUtils.isEmpty(serviceName)) {
            JbiComponentException exception = new JbiComponentException(1000,1004);
            exception.setMessage("Service Name is not set");
            exception.setUserMessage("服务名称没有设置");
            exception.setUserTitle("组件服务调用失败");
            throw exception;
        }
        LOGGER.debug("service size [{}]",ServiceResource.services.size());
        for (BusinessService service : ServiceResource.services) {
            LOGGER.debug("service cache name[{}]",service.getName());
            if(service.getName().equals(serviceName)) {
                Thread.currentThread().setContextClassLoader(null);
                new ServiceInvoker<>().setCamelContext(getComponent().getCamelContext());
                getEndpoint().setCamelContext(getComponent().getCamelContext());
                return service.process(getEndpoint(),exchange);
            }
        }
        JbiComponentException exception = new JbiComponentException(1000,1004);
        exception.setMessage("Component [" + componentName + "] service [" + serviceName + "] not find");
        exception.setUserMessage("本地组件[" + componentName + "]服务[" + serviceName + "]未找到");
        exception.setUserTitle("组件服务调用失败");
        throw exception;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
