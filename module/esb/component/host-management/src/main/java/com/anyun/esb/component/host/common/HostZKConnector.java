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

package com.anyun.esb.component.host.common;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.scheduling.NodeListener;
import com.anyun.cloud.tools.scheduling.ZookeeperFactory;
import com.anyun.common.jbi.component.ZkConnectLostListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/15/16
 */
public class HostZKConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostZKConnector.class);
    private final CountDownLatch connectedSignal = new CountDownLatch(1);
    private ZooKeeper zookeeper;
    private ZkConnectLostListener connectLostListener;

    public void close() throws InterruptedException {
        if (zookeeper != null)
            zookeeper.close();
    }

    public void connect(String host, int timeOut) throws Exception {
        zookeeper = new ZooKeeper(host, timeOut, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        connectedSignal.await(timeOut, TimeUnit.SECONDS);
        if (connectedSignal.getCount() != 0) {
            throw new IllegalStateException("ZooKeeper is not connected.");
        }
    }

    public void addNodeStatusListener(final NodeListener nodeListener) throws Exception {
        String path = nodeListener.getListenerPath();
        HostWatch defaultDataWatcher = new HostWatch(path, nodeListener);
        zookeeper.getChildren(path, defaultDataWatcher);
//        zookeeper.getChildren(path, true, new AsyncCallback.Children2Callback() {
//            @Override
//            public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
//                LOGGER.debug("chindren [{}]",children);
//            }
//        },null);
        LOGGER.info("Anyun cloud cluster scheduling listener [{}] added,path [{}]", nodeListener.getClass().getName(), path);
    }

    public void createNode(String path, String body, CreateMode createMode) throws Exception {
        byte[] data = null;
        if (StringUtils.isNotEmpty(body))
            data = body.getBytes();
        if (exist(path)) {
            zookeeper.setData(path, data, -1);
        } else {
            zookeeper.create(path, data, ZookeeperFactory.ALL_PERMS, createMode);
        }
    }

    public void createIfNodeNotExist(String path, String body, CreateMode createMode) throws Exception {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        byte[] data = null;
        if (StringUtils.isNotEmpty(body))
            data = body.getBytes();
        if (exist(path)) {
            return;
        } else {
            zookeeper.create(path, data, ZookeeperFactory.ALL_PERMS, createMode);
        }
    }


    public boolean exist(String path) throws Exception {
        return zookeeper.exists(path, null) != null;
    }

    public String getStringData(String path) throws Exception {
        byte[] data = getData(path);
        if (data == null)
            return null;
        return new String(data);
    }

    public byte[] getData(String path) throws Exception {
        if (zookeeper.exists(path, null) == null)
            return null;
        return zookeeper.getData(path, null, null);
    }

    public List<String> getChildren(String path) throws Exception {
        if (zookeeper.exists(path, null) == null)
            return null;
        return zookeeper.getChildren(path, null);
    }

    public ZooKeeper getZooKeeper() {
        return zookeeper;
    }

    public ZkConnectLostListener getConnectLostListener() {
        return connectLostListener;
    }

    public void setConnectLostListener(ZkConnectLostListener connectLostListener) {
        this.connectLostListener = connectLostListener;
    }
}
