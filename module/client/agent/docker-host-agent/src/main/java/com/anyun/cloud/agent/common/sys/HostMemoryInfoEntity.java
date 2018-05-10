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
public class HostMemoryInfoEntity extends BaseLinuxCommandEntity<HostMemoryInfoEntity> {

    @Command(value = "cat /proc/meminfo |grep 'MemTotal' |awk -F : '{print $2}' |sed 's/^[ \\t]*//g' | awk -F \" \" '{print $1}'")
    private long totalMemory;
    @Command(value = "cat /proc/meminfo |grep 'SwapTotal' |awk -F : '{print $2}' |sed 's/^[ \\t]*//g' | awk -F \" \" '{print $1}'")
    private long totalSwap;
    @Command(value = "cat /proc/meminfo |grep '^Buffers' |awk -F : '{print $2}' |sed 's/^[ \\t]*//g' | awk -F \" \" '{print $1}'")
    private long buffers;
    @Command(value = "cat /proc/meminfo |grep '^Cached' |awk -F : '{print $2}' |sed 's/^[ \\t]*//g' | awk -F \" \" '{print $1}'")
    private long cached;
    @Command(value = "free |grep - |awk -F : '{print $2}' |awk '{print $2}'")
    private long available;

    public long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public long getTotalSwap() {
        return totalSwap;
    }

    public void setTotalSwap(long totalSwap) {
        this.totalSwap = totalSwap;
    }

    public long getBuffers() {
        return buffers;
    }

    public void setBuffers(long buffers) {
        this.buffers = buffers;
    }

    public long getCached() {
        return cached;
    }

    public void setCached(long cached) {
        this.cached = cached;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "HostMemoryInfo{" + "totalMemory=" + totalMemory + ", totalSwap=" + totalSwap + ", buffers=" + buffers + ", cached=" + cached + ", available=" + available + '}';
    }
}
