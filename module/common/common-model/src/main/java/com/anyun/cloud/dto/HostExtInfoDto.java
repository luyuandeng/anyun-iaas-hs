package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class HostExtInfoDto extends AbstractEntity {
    private String hostId;//宿主机id
    private String cpuModel;//cpu型号
    private long cpuMhz;//CPU  MHZ
    private int physicalCpus;//物理cpu个数
    private int logicalCpus;//逻辑cpu个数
    private int cpuCores;//cpu核心
    private int cpuPresentMode;//0x86  1x64
    private long memoryTotal;//內存大小
    private long dockerDiskTotal;//docker 可用存儲大小
    private Date createDate;//创建时间
    private Date lastModifyDate;//最後修改時間

    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public int getCpuPresentMode() {
        return cpuPresentMode;
    }

    public void setCpuPresentMode(int cpuPresentMode) {
        this.cpuPresentMode = cpuPresentMode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getDockerDiskTotal() {
        return dockerDiskTotal;
    }

    public void setDockerDiskTotal(long dockerDiskTotal) {
        this.dockerDiskTotal = dockerDiskTotal;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public int getLogicalCpus() {
        return logicalCpus;
    }

    public void setLogicalCpus(int logicalCpus) {
        this.logicalCpus = logicalCpus;
    }

    public long getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(long memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public int getPhysicalCpus() {
        return physicalCpus;
    }

    public void setPhysicalCpus(int physicalCpus) {
        this.physicalCpus = physicalCpus;
    }

    public long getCpuMhz() {
        return cpuMhz;
    }

    public void setCpuMhz(long cpuMhz) {
        this.cpuMhz = cpuMhz;
    }
}
