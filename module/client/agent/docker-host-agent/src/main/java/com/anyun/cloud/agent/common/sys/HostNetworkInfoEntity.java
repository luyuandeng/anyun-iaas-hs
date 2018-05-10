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

import com.anyun.cloud.tools.bash.BashCommand;
import com.anyun.cloud.tools.db.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TwitchGG
 * @date 2015-4-24
 * @version 1.0
 */
public class HostNetworkInfoEntity implements BaseEntity<HostNetworkInfoEntity> {

    private List<HostNetworkInfoEntity> entitys;
    private String name;
    private String mode;
    private String speed;
    private String stat;

    public HostNetworkInfoEntity() {

    }

    public void initResult() {
        entitys = new ArrayList();
        String miiCmd = "mii-tool | sed 's/[:]/,/g'";
        String result = new BashCommand(miiCmd).exec();
        BufferedReader reader = new BufferedReader(new StringReader(result));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                String[] infos = line.split(",");
                HostNetworkInfoEntity entity = new HostNetworkInfoEntity();
                entity.setName(infos[0].trim());
                entity.setMode(infos[1].trim());
                entity.setSpeed(infos[2].trim());
                entity.setStat(infos[3].trim());
                entitys.add(entity);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public String asJson() {
        initResult();
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        Gson gson = gb.create();
        return gson.toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public void setEntitys(List<HostNetworkInfoEntity> entitys) {
        this.entitys = entitys;
    }

    public List<HostNetworkInfoEntity> getEntitys() {
        return entitys;
    }

    @Override
    public HostNetworkInfoEntity build() {
        return this;
    }
}
