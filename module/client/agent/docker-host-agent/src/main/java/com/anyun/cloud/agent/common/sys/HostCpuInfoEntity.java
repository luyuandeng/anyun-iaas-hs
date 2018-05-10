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
public class HostCpuInfoEntity extends BaseLinuxCommandEntity<HostCpuInfoEntity> {

    @Command(value = "grep 'model name' /proc/cpuinfo |uniq |awk -F : '{print $2}' |sed 's/^[ \\t]*//g' |sed 's/ \\+/ /g'")
    private String cpuModel;

    @Command(value = "grep 'physical id' /proc/cpuinfo |sort |uniq |wc -l")
    private int physicalCpus;
    
    @Command(value = " grep 'processor' /proc/cpuinfo |sort |uniq |wc -l")
    private int logicalCpus;
    
    @Command(value = "grep 'cpu cores' /proc/cpuinfo |uniq |awk -F : '{print $2}' |sed 's/^[ \\t]*//g'")
    private int cores;
    
    @Command(value = "getconf LONG_BIT")
    private int presentMode;

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public int getPhysicalCpus() {
        return physicalCpus;
    }

    public void setPhysicalCpus(int physicalCpus) {
        this.physicalCpus = physicalCpus;
    }

    public int getLogicalCpus() {
        return logicalCpus;
    }

    public void setLogicalCpus(int logicalCpus) {
        this.logicalCpus = logicalCpus;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getPresentMode() {
        return presentMode;
    }

    public void setPresentMode(int presentMode) {
        this.presentMode = presentMode;
    }

    @Override
    public String toString() {
        return "HostCpuInfo{" + "cpuModel=" + cpuModel + ", physicalCpus=" + physicalCpus + ", logicalCpus=" + logicalCpus + ", cores=" + cores + ", presentMode=" + presentMode + '}';
    }
}
