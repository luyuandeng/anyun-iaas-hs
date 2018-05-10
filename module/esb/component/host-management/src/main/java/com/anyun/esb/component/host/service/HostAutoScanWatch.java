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

package com.anyun.esb.component.host.service;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.scheduling.NodeListenerAdapter;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.HostZKConnector;
import com.anyun.esb.component.host.service.docker.DockerHostRegistService;
import com.anyun.esb.component.host.service.docker.impl.DockerHostRegistServiceImpl;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/25/16
 */
public class HostAutoScanWatch extends NodeListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostAutoScanWatch.class);
    private DockerHostRegistService dockerHostRegistService = new DockerHostRegistServiceImpl();

    @Override
    public String getListenerPath() {
        return "/anyuncloud/host";
    }

    @Override
    public void init() {
    }

    @Override
    public void process(WatchedEvent event) {
        LOGGER.debug("Event [{}]", event);
        HostNodes hostNodes = Env.env(HostNodes.class);
        try {
            List<String> children = Env.env(HostZKConnector.class).getChildren(event.getPath());
            LOGGER.debug("last nodes [{}]", hostNodes.getHosts());
            LOGGER.debug("fetch nodes [{}]", children);
            if (hostNodes.getEvent(children).equals(HostNodes.HostNodeEvent.NONE))
                return;
            else if (hostNodes.getEvent(children).equals(HostNodes.HostNodeEvent.ADD))
                addHost(children);
            else if (hostNodes.getEvent(children).equals(HostNodes.HostNodeEvent.DELETE))
                deleteHost(children);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
    }

    private void addHost(List<String> children) {
        try {
            HostNodes hostNodes = Env.env(HostNodes.class);
            List<String> hosts = hostNodes.getAddNodes(children);
            LOGGER.debug("Add hosts [{}]", hosts);
            hostNodes.add(hosts);
            for (String host : hosts) {
                String data = Env.env(HostZKConnector.class).getStringData("/anyuncloud/host/" + host);
                String[] info = StringUtils.getSplitValues(data, ":");
                new RegistThreadRunnable(dockerHostRegistService, host, info[0], info[1]).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteHost(List<String> children) {
        try {
            HostNodes hostNodes = Env.env(HostNodes.class);
            List<String> hosts = hostNodes.getDeleteNodes(children);
            LOGGER.debug("Delete hosts [{}]", hosts);
            hostNodes.delete(hosts);
            for (String ip : hosts) {
                new RemoveThreadRunnable(dockerHostRegistService, ip).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class RemoveThreadRunnable implements Runnable {
        private DockerHostRegistService dockerHostRegistService;
        private String ip;
        private Thread thread;

        public RemoveThreadRunnable(DockerHostRegistService dockerHostRegistService, String ip) {
            this.dockerHostRegistService = dockerHostRegistService;
            this.ip = ip;
        }

        public void start() {
            this.thread = new Thread(this);
            this.thread.start();
        }

        @Override
        public void run() {
            try {
                dockerHostRegistService.disconnectDockerHost(ip);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static class RegistThreadRunnable implements Runnable {
        private DockerHostRegistService dockerHostRegistService;
        private Thread thread;
        private String host;
        private String serial;
        private String ip;

        public RegistThreadRunnable(DockerHostRegistService dockerHostRegistService, String host, String serial, String ip) {
            this.dockerHostRegistService = dockerHostRegistService;
            this.serial = serial;
            this.ip = ip;
            this.host = host;
        }

        public void start() {
            this.thread = new Thread(this);
            this.thread.start();
        }

        @Override
        public void run() {
            try {
                dockerHostRegistService.connectHost(host, serial, ip);
            } catch (Exception ex) {
                LOGGER.error("Host regist error [{}]",ex.getMessage(),ex);
            }
        }
    }
}
