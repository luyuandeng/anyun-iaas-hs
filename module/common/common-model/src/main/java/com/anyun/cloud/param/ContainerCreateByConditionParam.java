package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sxt on 16-9-12.
 */
public class ContainerCreateByConditionParam extends AbstractEntity {
    /**
     * 宿主机 id （序列号）（必填）
     */
    private String host;

    /**
     * 宿主机 ip （管理ip）（必填）
     */
    private String ip;

    /**
     * 项目 id           （必填）
     */
    private String projectId;

    /**
     * 鏡像id  （必填）
     */
    private String imageId;

    /**
     * 容器用途 用途  比如WEB
     */
    private String purpose;

    /**
     * cup 核数限制  （非必填）
     */
    private int cpuCoreLimit;

    /**
     * 内存大小限制 （单位G） （非必填）
     */
    private int memoryLimit = 1;

    /**
     * 内存交换分区大小限制（单位G）
     */
    private int memorySwapLimit = 2;

    /**
     * 宿主机cpu型号 （非必填）
     */
    private String cpuFamily;

    /**
     * (单位 G)  宿主机内存总数 （非必填）
     */
    private long memoryTotal;
    /**
     * (单位 G)  宿主机内存未使用（非必填）
     */
    private long memoryUnused;


    /**
     * 容器显示名称 （非必填）
     */
    private String name;

    /**
     * 容器主机名称 （非必填）
     */
    private String hostName;

    /**
     * 是否拥有真正的 root权限 （非必填）
     * 0 ：true
     * 1 ：false
     * 使用该参数，container内的root拥有真正的root权限。
     * 否则，container内的root只是外部的一个普通用户权限。
     * privileged启动的容器，可以看到很多host上的设备，并且可以执行mount。
     * 甚至允许你在docker容器中启动docker容器。
     */
    private int privileged = 0;

    /**
     * 系统权限 根据CGROUP 规范定义  （非必填）
     */
    private Map<String, String> capAdd;

    /**
     * 安全参数 根据CGROUP 规范定义  （非必填）
     */
    private Map<String, String> securityOpt;

    /**
     * 内核 ulimit （非必填）
     */
    private Map<String, String> ulimit;

    /**
     * （非必填）
     */
    private String user = "root";

    /**
     * （非必填）
     */
    private String key;

    /**
     * 用户唯一标识 （非必填）
     */
    private String userUniqueId;

    /**
     * 计算方案主键   （非必填）
     */
    private String calculationSchemeId;

    /**
     * 磁盘方案主键 （非必填）
     */
    private String diskSchemeId;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getCpuCoreLimit() {
        return cpuCoreLimit;
    }

    public void setCpuCoreLimit(int cpuCoreLimit) {
        this.cpuCoreLimit = cpuCoreLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public int getMemorySwapLimit() {
        return memorySwapLimit;
    }

    public void setMemorySwapLimit(int memorySwapLimit) {
        this.memorySwapLimit = memorySwapLimit;
    }

    public String getCpuFamily() {
        return cpuFamily;
    }

    public void setCpuFamily(String cpuFamily) {
        this.cpuFamily = cpuFamily;
    }

    public long getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(long memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public long getMemoryUnused() {
        return memoryUnused;
    }

    public void setMemoryUnused(long memoryUnused) {
        this.memoryUnused = memoryUnused;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPrivileged() {
        return privileged;
    }

    public void setPrivileged(int privileged) {
        this.privileged = privileged;
    }

    public Map<String, String> getCapAdd() {
        return capAdd;
    }

    public void setCapAdd(Map<String, String> capAdd) {
        this.capAdd = capAdd;
    }

    public Map<String, String> getSecurityOpt() {
        return securityOpt;
    }

    public void setSecurityOpt(Map<String, String> securityOpt) {
        this.securityOpt = securityOpt;
    }

    public Map<String, String> getUlimit() {
        return ulimit;
    }

    public void setUlimit(Map<String, String> ulimit) {
        this.ulimit = ulimit;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getCalculationSchemeId() {
        return calculationSchemeId;
    }

    public void setCalculationSchemeId(String calculationSchemeId) {
        this.calculationSchemeId = calculationSchemeId;
    }

    public String getDiskSchemeId() {
        return diskSchemeId;
    }

    public void setDiskSchemeId(String diskSchemeId) {
        this.diskSchemeId = diskSchemeId;
    }
}
