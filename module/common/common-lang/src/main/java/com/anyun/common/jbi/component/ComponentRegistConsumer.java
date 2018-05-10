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

import com.anyun.common.jbi.JbiCommon;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/15/16
 */
public class ComponentRegistConsumer extends ZKBaseConsumerThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentRegistConsumer.class);
    private String componentName;

    public ComponentRegistConsumer(String componentName, Component component, Endpoint endpoint, Processor processor) {
        super(component, endpoint, processor);
        this.componentName = componentName;
    }

    public ComponentRegistConsumer(Component component, Endpoint endpoint, Processor processor) {
        super(component, endpoint, processor);
    }

    @Override
    protected void _run() throws Exception {
        String localMessageComponentSerialNumber = ComponentUtil.getMessageComponentSerialNumber();
        ComponentRegistInfo componentRegistInfo = new ComponentRegistInfo();
        componentRegistInfo.setMessageNode(localMessageComponentSerialNumber);
        String payload = JbiCommon.toJson(componentRegistInfo);
        String componentSerial = ComponentUtil.getSerialNumber(componentName);
        String zkPath = jedis.get("com.anyun.zookeeper.path.component") + componentName;
        zookeeperConnector.createIfNodeNotExist(zkPath, null, CreateMode.PERSISTENT);
        zkPath = zkPath + "/" + componentSerial;
        zookeeperConnector.createNode(zkPath, payload, CreateMode.EPHEMERAL);
        LOGGER.info("Regist Component to znode [{}]", zkPath);
        afterComponentRegistred();
    }

    public void afterComponentRegistred() throws Exception {

    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
