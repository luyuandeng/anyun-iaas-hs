package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-11-22.
 */
public class HostRealTimeDto extends AbstractEntity {
    private String host; //主机id
    private String name;//宿主机名称
    private String area;//宿主机所在区域
    private String cluster;//所在集群Id
    private String ip;  //宿主机管理ip
    private String cpuModel; //cpu 型号
    private int cpuCores; //cpu 核数
    private String cpuUsed;//   cpu   已使用百分比
    private String cpuUnUsed;// cpu   未使用百分比
    private int containerTotal; //容器总数
    private long memoryTotal;  // (单位 G)   内存总数
    private long memoryUsed;   // (单位 G)   已使用
    private long memoryUnused;  // (单位 G) 未使用
    private int status;//主机状态  1： 可用
    private String percentageUsed;// 已使用百分比
    private String percentageUnUsed;// 未使用百分比

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public String getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(String cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public String getCpuUnUsed() {
        return cpuUnUsed;
    }

    public void setCpuUnUsed(String cpuUnUsed) {
        this.cpuUnUsed = cpuUnUsed;
    }

    public int getContainerTotal() {
        return containerTotal;
    }

    public void setContainerTotal(int containerTotal) {
        this.containerTotal = containerTotal;
    }

    public long getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(long memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public long getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(long memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public long getMemoryUnused() {
        return memoryUnused;
    }

    public void setMemoryUnused(long memoryUnused) {
        this.memoryUnused = memoryUnused;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPercentageUsed() {
        return percentageUsed;
    }

    public void setPercentageUsed(String percentageUsed) {
        this.percentageUsed = percentageUsed;
    }

    public String getPercentageUnUsed() {
        return percentageUnUsed;
    }

    public void setPercentageUnUsed(String percentageUnUsed) {
        this.percentageUnUsed = percentageUnUsed;
    }
}
