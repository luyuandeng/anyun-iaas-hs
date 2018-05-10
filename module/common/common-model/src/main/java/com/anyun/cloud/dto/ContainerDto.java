package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by gaopeng on 16-6-7.
 */
public class ContainerDto extends AbstractEntity {
    /**
     * 容器主键
     */
    private String id;

    /**
     * 容器短id  充当真实容器名称
     */
    private String shortId;

    /**
     * 容器显示名称
     */
    private String name;

    /**
     * 容器主机名称
     */
    private String hostName;

    /**
     * 容器用途 用途   比如WEB
     */
    private String purpose;

    /**
     * 容器类型
     * 0 : 用户容器 (默认)   该容器是用户自己手动创建的， 用户可以完全操作，其他的为系统容器。
     * 1 : 应用发布   nginx 节点
     * 2 : 应用发布   load   节点
     * 3 : 数据库服务  单节点
     * 4 : 数据库服务   集群   haproxy         节点
     * 5 : 数据库服务  集群   mgm             节点
     * 6 : 数据库服务  集群   sql  和 ndbd     节点
     */
    private int type;

    /**
     * 容器状态
     * 1: created  (已创建)
     * 2:running (运行中)
     * 3:paused（暂停中）
     * 4:exited（已退出）
     */
    private int status;

    /**
     * 项目 Id
     */
    private String projectId;

    /**
     * 鏡像id
     */
    private String imageId;

    /**
     * 镜像名称
     */
    private String imageName;

    /**
     * cup 核数限制
     */
    private int cpuCoreLimit;

    /**
     * 内存大小限制 （单位G）
     */
    private int memoryLimit = 1;

    /**
     * 内存交换分区大小限制（单位G）
     */
    private int memorySwapLimit = 2;

    /**
     * 宿主机id（序列号）
     */
    private String hostId;

    /**
     * 宿主机cpu型号
     */
    private String cpuFamily;


    /**
     * 是否拥有真正的 root权限
     * 0 ：true
     * 1 ：false
     * 使用该参数，container内的root拥有真正的root权限。
     * 否则，container内的root只是外部的一个普通用户权限。
     * privileged启动的容器，可以看到很多host上的设备，并且可以执行mount。
     * 甚至允许你在docker容器中启动docker容器。
     */
    private int privileged = 0;

    /**
     * 计算方案 主键
     */
    private String calculationSchemeId;

    /**
     * 磁盘方案 主键
     */
    private String diskSchemeId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getCpuFamily() {
        return cpuFamily;
    }

    public void setCpuFamily(String cpuFamily) {
        this.cpuFamily = cpuFamily;
    }

    public int getPrivileged() {
        return privileged;
    }

    public void setPrivileged(int privileged) {
        this.privileged = privileged;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
