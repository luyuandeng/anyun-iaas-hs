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
public class HostPartionsUsageInfoEntity extends BaseLinuxCommandEntity<HostPartionsUsageInfoEntity>{
    private String partion;
    private long total;
    private long used;
    private long avail;
    private int percentage;
    private String mountedOn;
    
    public String getPartion() {
        return partion;
    }

    public void setPartion(String partion) {
        this.partion = partion;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getAvail() {
        return avail;
    }

    public void setAvail(long avail) {
        this.avail = avail;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getMountedOn() {
        return mountedOn;
    }

    public void setMountedOn(String mountedOn) {
        this.mountedOn = mountedOn;
    }

    @Override
    public String toString() {
        return "HostPartionsUsageInfoEntity{" + "partion=" + partion + ", total=" + total + ", used=" + used + ", avail=" + avail + ", percentage=" + percentage + ", mountedOn=" + mountedOn + '}';
    }
}
