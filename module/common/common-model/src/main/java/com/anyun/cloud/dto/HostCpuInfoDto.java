package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by sxt on 16-8-4.
 */
public class HostCpuInfoDto extends AbstractEntity {
    private List<String> cpuFamily;
    private List<Integer> cpuCoreLimit;

    public List<String> getCpuFamily() {
        return cpuFamily;
    }

    public void setCpuFamily(List<String> cpuFamily) {
        this.cpuFamily = cpuFamily;
    }

    public List<Integer> getCpuCoreLimit() {
        return cpuCoreLimit;
    }

    public void setCpuCoreLimit(List<Integer> cpuCoreLimit) {
        this.cpuCoreLimit = cpuCoreLimit;
    }
}
