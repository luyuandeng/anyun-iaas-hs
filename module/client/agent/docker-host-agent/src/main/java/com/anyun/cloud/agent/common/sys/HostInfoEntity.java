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

package com.anyun.cloud.agent.common.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/17/16
 */
public class HostInfoEntity extends BaseJsonEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostInfoEntity.class);

    private HostCpuInfoEntity cpuInfo;
    private HostMemoryInfoEntity memoryInfo;
    private HostDiskInfoEntity diskInfo;
    private HostNetworkInfoEntity networkInfo;
    private OperateSystemInfoEntity osInfo;

    @Override
    public HostInfoEntity build() {
        this.cpuInfo = new LinuxCommandEntityBuilder<HostCpuInfoEntity>().build(new HostCpuInfoEntity());
        LOGGER.debug("CPU INFO [{}]",cpuInfo);
        this.memoryInfo = new LinuxCommandEntityBuilder<HostMemoryInfoEntity>().build(new HostMemoryInfoEntity());
        LOGGER.debug("MEMORY INFO [{}]",memoryInfo);
        this.diskInfo = new LinuxCommandEntityBuilder<HostDiskInfoEntity>().build(new HostDiskInfoEntity());
        LOGGER.debug("DISK INFO [{}]",diskInfo);
        this.networkInfo = new LinuxCommandEntityBuilder<HostNetworkInfoEntity>().build(new HostNetworkInfoEntity());
        LOGGER.debug("NETWORK INFO [{}]",networkInfo);
        this.osInfo = new LinuxCommandEntityBuilder<OperateSystemInfoEntity>().build(new OperateSystemInfoEntity());
        LOGGER.debug("OS INFO [{}]",osInfo);
        return this;
    }

    public HostCpuInfoEntity getCpuInfo() {
        return cpuInfo;
    }

    public HostMemoryInfoEntity getMemoryInfo() {
        return memoryInfo;
    }

    public HostDiskInfoEntity getDiskInfo() {
        return diskInfo;
    }

    public HostNetworkInfoEntity getNetworkInfo() {
        return networkInfo;
    }

    public OperateSystemInfoEntity getOsInfo() {
        return osInfo;
    }

    @Override
    public String toString() {
        return "HostInfo{" + "cpuInfo=" + cpuInfo + ", memoryInfo=" + memoryInfo +
                ", diskInfo=" + diskInfo + ", networkInfo=" + networkInfo + ", osInfo=" + osInfo + '}';
    }
}
