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

package com.anyun.esb.component.host;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.common.jbi.component.*;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.service.ServiceResource;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/13/16
 */
public class ServicesRegistConsumer extends ConsumerThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicesRegistConsumer.class);
    private String componentName = "anyun-host";

    private ZKConnector zookeeperConnector;

    public ServicesRegistConsumer(Component component, Endpoint endpoint, Processor processor) {
        super(component, endpoint, processor);
    }

    @Override
    public void run() {
        try {
            Jedis jedis = new Jedis("redis.dev.anyuncloud.com", 6379);
            String zkString = jedis.get("com.anyun.zookeeper.address");
            String timeout = jedis.get("com.anyun.zookeeper.timeout");
            zookeeperConnector = new ZKConnector();
            zookeeperConnector.setConnectLostListener(new ZkConnectLostListener() {
                @Override
                public void afterConnectLost(ZKConnector connector) {
                    try {
                        stop();
                        if (!isStart())
                            return;
                        new ServicesRegistConsumer(getComponent(), getEndpoint(), getProcessor()).start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            zookeeperConnector.connect(zkString, Long.valueOf(timeout).intValue());
            LOGGER.info("Anyun cloud scheduling server [{}] connected", zkString);
            String localMessageComponentSerialNumber = ComponentUtil.getMessageComponentSerialNumber();
            ComponentRegistInfo componentRegistInfo = new ComponentRegistInfo();
            componentRegistInfo.setMessageNode(localMessageComponentSerialNumber);
            String payload = JbiCommon.toJson(componentRegistInfo);
            String componentSerial = ComponentUtil.getSerialNumber(componentName);
            String zkPath = jedis.get("com.anyun.zookeeper.path.component") + componentName;
            zookeeperConnector.createIfNodeNotExist(zkPath, null, CreateMode.PERSISTENT);
            zkPath = zkPath + "/" + componentSerial;
            zookeeperConnector.createNode(zkPath, payload, CreateMode.EPHEMERAL);
            LOGGER.info("Regist AnyunCloud business component to znode [{}]", zkPath);
            registComponentServices(jedis, componentSerial);
            Env.set(ZKConnector.class,zookeeperConnector);
        } catch (Exception exception) {
            LOGGER.warn("Component and services regist error [" + exception.getMessage() + "]", exception);
            stop();
        }
    }

    private void registComponentServices(Jedis jedis, String componentId) throws Exception {
        CamelContext context = getEndpoint().getCamelContext();
        List<Class<? extends BusinessService>> serviceClasses =
                ServiceResource.getComponentServiceClasses(context.getPackageScanClassResolver());
        String zkServicePath = jedis.get("com.anyun.zookeeper.path.service");
        for (Class<? extends BusinessService> aClass : serviceClasses) {
            try {
                BusinessService businessService = ServiceResource.newService(aClass);
                String serviceId = businessService.getServiceId();
                String serviceName = businessService.getName();
                if (StringUtils.isEmpty(serviceId) || StringUtils.isEmpty(serviceName))
                    throw new Exception("Service id or name is not set [" + businessService.getClass().getName() + "]");
                ComponentServiceRegistInfo serviceRegistInfo = new ComponentServiceRegistInfo();
                serviceRegistInfo.setComponent(componentId);
                String payload = JbiCommon.toJson(serviceRegistInfo);
                String path = zkServicePath + serviceName;
                zookeeperConnector.createIfNodeNotExist(path, null, CreateMode.PERSISTENT);
                path = path + "/" + serviceId;
                zookeeperConnector.createNode(path, payload, CreateMode.EPHEMERAL);
                ServiceResource.services.add(businessService);
                LOGGER.info("Regist AnyunCloud business component service to znode [{}-{}]", businessService.getClass().getName(), serviceId);
            } catch (Exception ex) {
                LOGGER.warn("Component service class [{}] regist error [{}]", aClass.getName(), ex.getMessage());
            }
        }
    }

    @Override
    public void stop() {
        try {
            LOGGER.debug("Shutdown zookeeper connection");
            if (zookeeperConnector != null)
                zookeeperConnector.close();
            super.stop();
        } catch (Exception ex) {
        }
    }

    public ZKConnector getZookeeperConnector() {
        return zookeeperConnector;
    }
}
