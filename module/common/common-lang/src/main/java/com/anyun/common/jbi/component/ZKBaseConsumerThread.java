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

import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/15/16
 */
public abstract class ZKBaseConsumerThread extends ConsumerThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZKBaseConsumerThread.class);
    protected ZKConnector zookeeperConnector;
    protected Jedis jedis;
    private String zookeeperConnectString;

    public ZKBaseConsumerThread(Component component, Endpoint endpoint, Processor processor) {
        super(component, endpoint, processor);
    }

    @Override
    public void run() {
        try {
            jedis = new Jedis("redis.dev.anyuncloud.com", 6379);
            zookeeperConnectString = jedis.get("com.anyun.zookeeper.address");
            String timeout = jedis.get("com.anyun.zookeeper.timeout");
            zookeeperConnector = new ZKConnector();
            zookeeperConnector.connect(zookeeperConnectString, Long.valueOf(timeout).intValue());
            LOGGER.info("Anyun cloud scheduling server [{}] connected", zookeeperConnectString);
            _run();
        } catch (Exception exception) {
            LOGGER.warn("ZKBaseConsumerThread runnable error [" + exception.getMessage() + "]", exception);
        }
    }

    protected abstract void _run() throws Exception;

    @Override
    public void stop() {
        try {
            LOGGER.debug("Shutdown zookeeper connection");
            zookeeperConnector.close();
            super.stop();
        } catch (Exception ex) {
        }
    }

    public ZKConnector getZookeeperConnector() {
        return zookeeperConnector;
    }

    public String getZookeeperConnectString() {
        return zookeeperConnectString;
    }

    public void setZookeeperConnectString(String zookeeperConnectString) {
        this.zookeeperConnectString = zookeeperConnectString;
    }
}
