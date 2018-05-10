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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anyun.cloud.agent.common.sys;

/**
 *
 * @author TwitchGG
 * @date 2015-4-24
 * @version 1.0
 */
public class DiskMountCommandProcess extends BaseMultiCommandProcess<HostDiskMountInfoEntity> {

    public DiskMountCommandProcess() {
    }

    @Override
    protected HostDiskMountInfoEntity getLineObject(String line, String[] args) {
        String[] infos = line.split("\\ ");
        HostDiskMountInfoEntity hdmi = new HostDiskMountInfoEntity();
        hdmi.setSrc(infos[0]);
        hdmi.setDest(infos[2]);
        hdmi.setType(infos[4]);
        String privilege = infos[5].replace("(", "").replace(")", "");
        hdmi.setPrivilege(privilege);
        if (!hdmi.getSrc().startsWith("/dev/")) {
            return null;
        }
        if (hdmi.getSrc().startsWith("/dev/loop")) {
            return null;
        }
        return hdmi;
    }
}
