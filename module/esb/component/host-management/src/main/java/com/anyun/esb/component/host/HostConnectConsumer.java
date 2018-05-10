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

import com.anyun.common.jbi.component.ConsumerThread;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.HostZKConnector;
import com.anyun.esb.component.host.service.HostAutoScanWatch;
import com.anyun.esb.component.host.service.HostNodes;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/23/16
 */
public class HostConnectConsumer extends ConsumerThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostConnectConsumer.class);
    private HostZKConnector zookeeperConnector;
    private HostAutoScanWatch hostAutoScanWatch;

    public HostConnectConsumer(Component component, Endpoint endpoint, Processor processor) {
        super(component, endpoint, processor);
    }

    @Override
    public void run() {
        try {
            Jedis jedis = Env.env(Jedis.class);
            String zkString = jedis.get("com.anyun.zookeeper.address");
            String timeout = jedis.get("com.anyun.zookeeper.timeout");
            hostAutoScanWatch = new HostAutoScanWatch();
            zookeeperConnector = new HostZKConnector();
            Env.export(HostZKConnector.class, zookeeperConnector);
            HostNodes hostNodes = new HostNodes();
            Env.export(HostNodes.class, hostNodes);
            zookeeperConnector.connect(zkString, Long.valueOf(timeout).intValue());
            zookeeperConnector.addNodeStatusListener(hostAutoScanWatch);
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.warn("Host connect error [" + exception.getMessage() + "]", exception);
            stop();
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
}
