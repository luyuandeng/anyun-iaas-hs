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

import com.anyun.cloud.tools.db.BaseEntity;
import com.google.gson.Gson;

/**
 *
 * @author TwitchGG
 * @date 2015-4-24
 * @version 1.0
 */
public class HostDiskMountInfoEntity implements  BaseEntity<HostDiskMountInfoEntity>{
    private String src;
    private String dest;
    private String type;
    private String privilege;

    @Override
    public String asJson() {
        return new Gson().toJson(this);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HostDiskMountInfo{" + "src=" + src + ", dest=" + dest + ", type=" + type + ", privilege=" + privilege + '}';
    }

    @Override
    public HostDiskMountInfoEntity build() {
        return this;
    }
}
