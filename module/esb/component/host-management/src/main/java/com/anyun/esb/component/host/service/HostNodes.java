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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/25/16
 */
public class HostNodes {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostNodes.class);
    private List<String> hosts;

    public HostNodes() {
        hosts = new ArrayList<>();
    }

    public HostNodeEvent getEvent(List<String> children) {
        if (children.size() > hosts.size())
            return HostNodeEvent.ADD;
        else if (children.size() < hosts.size())
            return HostNodeEvent.DELETE;
        else
            return HostNodeEvent.NONE;
    }

    public List<String> getAddNodes(List<String> children) {
        List<String> list = new ArrayList<>();
        for (String c : children) {
            if (!exist(hosts, c)) {
                list.add(c);
            }
        }
        return list;
    }

    public List<String> getDeleteNodes(List<String> children) {
        List<String> list = new ArrayList<>();
        for (String c : hosts) {
            if (!exist(children, c)) {
                list.add(c);
            }
        }
        return list;
    }

    public void add(List<String> addHosts) {
        hosts.addAll(addHosts);
    }

    public void delete(List<String> deletehosts) {
        hosts.removeAll(deletehosts);
    }

    private boolean exist(List<String> alls, String host) {
        for (String host_ : alls) {
            if (host_.equals(host))
                return true;
        }
        return false;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public static enum HostNodeEvent {
        ADD, DELETE, NONE
    }

}
