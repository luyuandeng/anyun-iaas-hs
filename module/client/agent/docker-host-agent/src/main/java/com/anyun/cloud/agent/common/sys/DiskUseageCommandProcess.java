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
public class DiskUseageCommandProcess extends BaseMultiCommandProcess<HostPartionsUsageInfoEntity>{

    @Override
    protected HostPartionsUsageInfoEntity getLineObject(String line, String[] args) {
        String[] infos = line.split("\\ ");
        HostPartionsUsageInfoEntity entity = new HostPartionsUsageInfoEntity();
        entity.setPartion(infos[0]);
        entity.setTotal(Long.valueOf(infos[1]));
        entity.setUsed(Long.valueOf(infos[2]));
        entity.setAvail(Long.valueOf(infos[3]));
        entity.setPercentage(Integer.valueOf(infos[4].replace("%", "")));
        entity.setMountedOn(infos[5]);
        if(!entity.getPartion().startsWith("/dev/"))
            return null;
        if(entity.getPartion().startsWith("/dev/loop"))
            return null;
        return entity;
    }
}
