package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 16-9-12.
 */
public class MonitorOverviewDto extends AbstractEntity{
    private int totalHost;
    private int runingHost;
    private int stopHost;
    private int totalContainer;
    private int runningContainer;
    private int stopContainer;
    private int totalImages;
    private int registryImages;
    private int unregistryImages;
    private String size;  //容量（G）
    private String used;  //已用（G）
    private String avail; //可用 （G）
    private String usedPercentage;  //已用百分比
    private int totalVolume;     //卷总量
    private int attachedVolume;  //已挂载卷数量
    private int detachedVolume;  //未挂载卷数量

    public int getTotalHost() {
        return totalHost;
    }

    public void setTotalHost(int totalHost) {
        this.totalHost = totalHost;
    }

    public int getRuningHost() {
        return runingHost;
    }

    public void setRuningHost(int runingHost) {
        this.runingHost = runingHost;
    }

    public int getStopHost() {
        return stopHost;
    }

    public void setStopHost(int stopHost) {
        this.stopHost = stopHost;
    }

    public int getTotalContainer() {
        return totalContainer;
    }

    public void setTotalContainer(int totalContainer) {
        this.totalContainer = totalContainer;
    }

    public int getRunningContainer() {
        return runningContainer;
    }

    public void setRunningContainer(int runningContainer) {
        this.runningContainer = runningContainer;
    }

    public int getStopContainer() {
        return stopContainer;
    }

    public void setStopContainer(int stopContainer) {
        this.stopContainer = stopContainer;
    }

    public int getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(int totalImages) {
        this.totalImages = totalImages;
    }

    public int getRegistryImages() {
        return registryImages;
    }

    public void setRegistryImages(int registryImages) {
        this.registryImages = registryImages;
    }

    public int getUnregistryImages() {
        return unregistryImages;
    }

    public void setUnregistryImages(int unregistryImages) {
        this.unregistryImages = unregistryImages;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public String getUsedPercentage() {
        return usedPercentage;
    }

    public void setUsedPercentage(String usedPercentage) {
        this.usedPercentage = usedPercentage;
    }

    public int getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(int totalVolume) {
        this.totalVolume = totalVolume;
    }

    public int getAttachedVolume() {
        return attachedVolume;
    }

    public void setAttachedVolume(int attachedVolume) {
        this.attachedVolume = attachedVolume;
    }

    public int getDetachedVolume() {
        return detachedVolume;
    }

    public void setDetachedVolume(int detachedVolume) {
        this.detachedVolume = detachedVolume;
    }
}
